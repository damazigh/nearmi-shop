package org.nearmi.shop.service;

import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.dto.in.SearchShopDto;
import org.springframework.data.domain.Pageable;

/**
 * contains business method for managing, creating, deleting a shop
 *
 * @author adjebarri
 * @since 0.1.0
 */
public interface IShopService {
    /**
     * @param shop an object that holds shop properties
     */
    void create(ShopDto shop);

    /**
     * method that allows to search for shop by criteria
     *
     * @param searchShopDto object that holds criteria
     * @param pageable      pagination limit and offset
     * @return a collection of {@link Shop} matching search criteria
     */
    PaginatedSearchResult<Shop> search(SearchShopDto searchShopDto, Pageable pageable);
}
