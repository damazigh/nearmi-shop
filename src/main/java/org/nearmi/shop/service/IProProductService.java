package org.nearmi.shop.service;

import org.nearmi.shop.dto.in.CreateProductCategoryDto;
import org.nearmi.shop.dto.in.CreateProductDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * business interface providing methods allowing professional user to manage their shops
 *
 * @author A.Djebarri
 * @since 0.1.0
 */
public interface IProProductService {
    String createProduct(String shopId, CreateProductDto productDto, MultipartFile[] files);

    /**
     * business method for creating a new product category related to shopId
     *
     * @param shopId             id of targeted shop
     * @param productCategoryDto wrapper that holds new category data
     * @return id of the created category
     */
    String createProductCategory(String shopId, CreateProductCategoryDto productCategoryDto);
}
