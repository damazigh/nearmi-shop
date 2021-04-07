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
}
