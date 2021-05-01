package org.nearmi.shop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.nearmi.core.exception.MiException;
import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.core.repository.MiProUserRepository;
import org.nearmi.core.resource.GeneralResKey;
import org.nearmi.core.service.IUploadService;
import org.nearmi.shop.repository.ProductRepository;
import org.nearmi.shop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MiProUserRepository proUserRepository;
    @Autowired
    private IUploadService uploadService;

    @Override
    public Product getProductByName(String shopId, String productName) {
        log.debug("loading product with name {} for shop with id {}", productName, shopId);
        Optional<Product> opProduct = productRepository.findByNameAndProductOwnerId(productName, shopId);
        if (opProduct.isEmpty()) {
            log.debug("product with name {} belonging to shop {} not found", productName, shopId);
            throw new MiException(GeneralResKey.NMI_G_0002);
        }
        return opProduct.get();
    }

    @Override
    public byte[] loadProductImage(String shopId, String productName, String imageName) {
        log.debug("loading image with nam {}e for product with name {} for shop with id {}", imageName, productName, shopId);
        Product product = this.getProductByName(shopId, productName);
        String path = product.getMetadata().stream().filter(m -> m.getName().equals(imageName)).map(m -> m.getPath())
                .findFirst().orElseThrow(() -> new MiException(GeneralResKey.NMI_G_0002));
        return uploadService.load(path);
    }

}
