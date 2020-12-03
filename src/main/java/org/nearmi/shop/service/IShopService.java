package org.nearmi.shop.service;

import org.nearmi.shop.dto.ShopDto;

/**
 * contains business method for managing, creating, deleting a shop
 * @author adjebarri
 * @since 0.1.0
 */
public interface IShopService {
    /**
     *
     * @param shop an object that holds shop properties
     */
    void create(ShopDto shop);
}
