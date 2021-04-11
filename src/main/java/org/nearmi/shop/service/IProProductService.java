package org.nearmi.shop.service;

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
     * check if the provided value is unique within the shop
     *
     * @param field  field to check withing the collection (product)
     * @param value  value to check for unicity
     * @param shopId targeted shop
     * @return true if the value doesn't exist false either
     */
    boolean isUnique(String field, String value, String shopId);
}
