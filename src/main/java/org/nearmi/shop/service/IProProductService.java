package org.nearmi.shop.service;

import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.shop.dto.in.CreateProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * business interface providing methods allowing professional user to manage their shops
 *
 * @author A.Djebarri
 * @since 0.1.0
 */
public interface IProProductService {
    /**
     * create a product and link it to targeted shop
     *
     * @param shopId     targeted shop
     * @param productDto product dto
     * @param files      a list of image
     */
    void createProduct(String shopId, CreateProductDto productDto, MultipartFile[] files);

    /**
     * get a paginated search set of product
     *
     * @param shopId   targeted shop
     * @param pageable hold pagination offset (page index) and limit (page size)
     * @return paginated search result
     */
    PaginatedSearchResult<Product> getProducts(String shopId, Pageable pageable);

    /**
     * check if the provided value is unique within the shop
     *
     * @param field  field to check withing the collection (product)
     * @param value  value to check for unicity
     * @param shopId targeted shop
     * @return true if the value doesn't exist false either
     */
    boolean isUnique(String field, String value, String shopId);

    /**
     * delete a product belonging to a shop with its id
     *
     * @param shopId    targeted shop
     * @param productId targeted product
     */
    void delete(String shopId, String productId);
}
