package org.nearmi.shop.service.impl;

import org.nearmi.core.mongo.document.MiProUser;
import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.core.mongo.document.shopping.ProductCategory;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.repository.MiProUserRepository;
import org.nearmi.core.security.CoreSecurity;
import org.nearmi.core.service.impl.UploadService;
import org.nearmi.shop.dto.in.CreateProductCategoryDto;
import org.nearmi.shop.dto.in.CreateProductDto;
import org.nearmi.shop.repository.ProductCategoryRepository;
import org.nearmi.shop.repository.ProductRepository;
import org.nearmi.shop.repository.ShopRepository;
import org.nearmi.shop.service.IProProductService;
import org.nearmi.shop.validator.ShopValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.nearmi.core.validator.Validator.*;

@Service
public class ProProductServiceImpl implements IProProductService {
    @Autowired
    private MiProUserRepository proUserRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Value("${nearmi.config.acceptedImageMime}")
    private String acceptedMime;

    @Override
    public String createProduct(String shopId, CreateProductDto productDto, MultipartFile[] files) {
        notNull(productDto, "product dto");
        notEmpty(productDto.getDescription(), "product description");
        notEmpty(productDto.getName(), "product name");
        positiveNumber(productDto.getPrice(), "product price");
        MiProUser proUser = proUserRepository.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop shop = ShopValidator.validateShopBelongToUser(proUser, shopId);
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setAmount(productDto.getInitialAmount());
        List<String> metadata = Arrays.stream(files).map(file -> uploadService.upload(file, proUser.getId(), shopId, acceptedMime))
                .collect(Collectors.toList());
        product.setMetadata(metadata);
        shop.getProducts().add(product);
        Product saved = productRepository.save(product);
        shopRepository.save(shop);
        return saved.getId();
    }

    @Override
    public String createProductCategory(String shopId, CreateProductCategoryDto productCategoryDto) {
        notNull(productCategoryDto, "product category");
        notEmpty(productCategoryDto.getName(), "product category name");
        MiProUser proUser = proUserRepository.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop shop = ShopValidator.validateShopBelongToUser(proUser, shopId);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(productCategoryDto.getName());
        if (productCategoryDto.getOrder() > 0) {
            productCategory.setOrder(productCategoryDto.getOrder());
        } else {
            productCategory.setOrder(calcOrder(shop));
        }
        shop.addProductCategory(productCategory);
        ProductCategory saved = productCategoryRepository.save(productCategory);
        shopRepository.save(shop);
        return saved.getId();
    }
    
    private int calcOrder(Shop shop) {
        if (shop.getProductCategories() != null && shop.getProductCategories().isEmpty()) {
            return shop.getProductCategories().size();
        }
        return 1;
    }
}