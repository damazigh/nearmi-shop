package org.nearmi.shop.service;

import org.nearmi.core.mongo.document.shopping.Product;

public interface IProductService {

    Product getProductByName(String shopId, String productName);

    byte[] loadProductImage(String shopId, String productName, String imageName);
}
