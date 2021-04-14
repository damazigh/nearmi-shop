package org.nearmi.shop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.mongo.document.MiProUser;
import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.core.mongo.document.shopping.ProductCategory;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.mongo.document.technical.ImageMetadata;
import org.nearmi.core.repository.MiProUserRepository;
import org.nearmi.core.security.CoreSecurity;
import org.nearmi.core.service.impl.UploadService;
import org.nearmi.shop.dto.in.CreateProductDto;
import org.nearmi.shop.dto.in.ImageBoundariesDto;
import org.nearmi.shop.repository.ProductCategoryRepository;
import org.nearmi.shop.repository.ProductRepository;
import org.nearmi.shop.repository.ShopRepository;
import org.nearmi.shop.rest.ShopResKey;
import org.nearmi.shop.service.IProProductService;
import org.nearmi.shop.service.ServiceConstant;
import org.nearmi.shop.validator.ShopValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.nearmi.core.validator.Validator.*;

@Service
@Slf4j
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
    private String[] acceptedMime;

    @Value("${nearmi.config.product-image-sub-folders}")
    private String[] productImageSubFolders;

    @Override
    public void createProduct(String shopId, CreateProductDto productDto, MultipartFile[] files) {
        log.info("trying to create a new product for shop {}", shopId);
        notNull(productDto, "product dto");
        notEmpty(productDto.getName(), "product name");
        positiveNumber(productDto.getPrice(), "product price");
        notNull(files, "images list");
        equal(productDto.getBoundaries().size(), files.length, ShopResKey.NMI_S_0004);
        log.debug("product creation primary validation: ok");
        log.debug("submitted product {} images", files.length);
        MiProUser proUser = proUserRepository.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop shop = ShopValidator.validateShopBelongToUser(proUser, shopId);
        Product product = new Product(shop);
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        if (productDto.getQuantity() == null) {
            log.debug("quantity not provided for product");
            product.setAmount(ServiceConstant.QUANTITY_NOT_SET);
        } else {
            product.setAmount(productDto.getQuantity());
        }
        int i = 0;
        List<ImageMetadata> metadata = new ArrayList<>();
        if (productDto.getProductCategory() != null) {
            ProductCategory pc = productCategoryRepository.findById(productDto.getProductCategory())
                    .orElse(null);
            if (pc != null) {
                product.setProductCategory(pc);
            } else {
                log.warn("category with id {} was not found, ignoring category", productDto.getProductCategory());
            }
        }
        for (MultipartFile file : files) {
            ImageBoundariesDto boundary = productDto.getBoundaries().get(i);
            notNull(boundary, String.format("boundaries (of image at index %1s)", i));
            String path = uploadService.uploadInSubFolder(file, proUser.getId(), shopId, productImageSubFolders, acceptedMime);
            log.debug("stored file with original filename {} in path {}", file.getOriginalFilename(), path);
            i++;
            metadata.add(new ImageMetadata(proUser.getId(), false, path, boundary.getWidth(), boundary.getHeight()));
        }
        product.setMetadata(metadata);
        Product saved = productRepository.save(product);
        log.debug("created new product with id {}", saved.getId());
    }

    @Override
    public PaginatedSearchResult<Product> getProducts(String shopId, Pageable pageable) {
        log.info("search product for shop {} - page offset {} - page size {}", shopId, pageable.getOffset(), pageable.getPageSize());
        MiProUser proUser = proUserRepository.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop shop = ShopValidator.validateShopBelongToUser(proUser, shopId);
        return PaginatedSearchResult.of(productRepository.findByProductOwnerId(shopId, pageable));
    }

    @Override
    public boolean isUnique(String field, String value, String shopId) {
        return productRepository.isUnique(value, field, shopId);
    }

    @PostConstruct
    public void post() {
        log.info("{} configured accepted mime  type  {} ", acceptedMime.length, acceptedMime);
    }
}
