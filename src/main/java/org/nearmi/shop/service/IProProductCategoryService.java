package org.nearmi.shop.service;

import org.nearmi.core.mongo.document.shopping.ProductCategory;
import org.nearmi.shop.dto.in.CreateProductCategoryDto;

import java.util.Collection;
import java.util.List;

/**
 * interface of product category service
 * it provides methods for managing product category (creation, deletion, update...)
 *
 * @author adjebarri
 * @since 0.1.0
 */
public interface IProProductCategoryService {


    /**
     * business method for creating a new product category related to shopId
     *
     * @param shopId               id of targeted shop
     * @param productCategoriesDto wrapper that holds a list of categories to create
     */
    void createProductCategories(String shopId, Collection<CreateProductCategoryDto> productCategoriesDto);

    /**
     * list already saved product category for a given shop
     *
     * @param shopId given shop
     * @return list of product category
     */
    List<ProductCategory> listProductCategory(String shopId);

    /**
     * delete product category
     *
     * @param shopId            target shop which belongs the product category to delete
     * @param productCategoryId targeted product category to delete
     */
    void deleteProductCategory(String shopId, String productCategoryId);
}
