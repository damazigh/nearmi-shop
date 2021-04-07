package org.nearmi.shop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.nearmi.core.exception.MiException;
import org.nearmi.core.mongo.document.MiProUser;
import org.nearmi.core.mongo.document.shopping.ProductCategory;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.repository.MiProUserRepository;
import org.nearmi.core.security.CoreSecurity;
import org.nearmi.shop.dto.in.CreateProductCategoryDto;
import org.nearmi.shop.repository.ProductCategoryRepository;
import org.nearmi.shop.repository.ShopRepository;
import org.nearmi.shop.rest.ShopResKey;
import org.nearmi.shop.service.IProProductCategoryService;
import org.nearmi.shop.validator.ShopValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static org.nearmi.core.validator.Validator.*;

/**
 * implementation of {@link org.nearmi.shop.service.IProProductCategoryService}
 */
@Slf4j
@Service
public class ProProductCategoryServiceImpl implements IProProductCategoryService {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private MiProUserRepository proUserRepository;

    @Override
    public void createProductCategories(String shopId, Collection<CreateProductCategoryDto> productCategoriesDto) {
        notNull(productCategoriesDto, "product category");
        log.info("Creating {} product categories for shop with id {}", productCategoriesDto.size(), shopId);
        MiProUser proUser = proUserRepository.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop shop = ShopValidator.validateShopBelongToUser(proUser, shopId);
        productCategoriesDto.forEach(productCategoryDto -> {
            notEmpty(productCategoryDto.getName(), "product category name");
            log.debug("creating product category with name {}", productCategoryDto.getName());
            ProductCategory productCategory = new ProductCategory();
            productCategory.setName(productCategoryDto.getName());
            if (productCategoryDto.getOrder() > 0) {
                productCategory.setOrder(productCategoryDto.getOrder());
            } else {
                productCategory.setOrder(calcOrder(shop));
            }
            shop.addProductCategory(productCategory);
            log.debug("added category {} at order {} to shop", productCategoryDto.getName(), productCategoryDto.getOrder());
            productCategoryRepository.save(productCategory);
            log.debug("category saved");
        });
        shopRepository.save(shop);
    }


    @Override
    public List<ProductCategory> listProductCategory(String shopId) {
        MiProUser proUser = proUserRepository.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop shop = ShopValidator.validateShopBelongToUser(proUser, shopId);
        return shop.getProductCategories();
    }

    @Override
    public void deleteProductCategory(String shopId, String productCategoryId) {
        notEmpty(shopId, "shopId");
        notEmpty(productCategoryId, productCategoryId);
        log.info("requested deletion of category with id {} belonging to shop with id {}", productCategoryId, shopId);
        MiProUser proUser = proUserRepository.findByUsername(CoreSecurity.token().getPreferredUsername());
        Shop shop = ShopValidator.validateShopBelongToUser(proUser, shopId);
        if (shop.getProductCategories().stream().noneMatch(pc -> pc.getId().equals(productCategoryId))) {
            log.debug("product category with id {} not found or not belongs to shop with id {}", productCategoryId, shopId);
            throw new MiException(ShopResKey.NMI_S_0003);
        }
        // TODO il faudrait rebasculer tous les produits attachés à cette catégorie
        productCategoryRepository.deleteById(productCategoryId);
        log.debug("deleted product category with id {}", productCategoryId);
    }

    private int calcOrder(Shop shop) {
        if (shop.getProductCategories() != null && !shop.getProductCategories().isEmpty()) {
            return shop.getProductCategories().size() + 1;
        }
        return 1;
    }
}
