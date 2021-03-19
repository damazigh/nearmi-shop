package org.nearmi.shop.service;

import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.dto.in.SearchShopDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

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

    /**
     * update shop image
     *
     * @param files
     */
    void updateImages(MultipartFile[] files, String shopId, String rootImage);

    /**
     * load presentation image of shop from its id if exist
     *
     * @param shopId
     * @return
     */
    byte[] loadImage(String shopId, String name);

    /**
     * retrieve belonging shop to authenticated user
     *
     * @return a collection of {@link Shop}
     */
    Collection<Shop> getBelongingShop();

    /**
     * get shop by its id
     *
     * @param shopId targeted shop id
     * @return shop if found and belong to authenticated user
     */
    Shop getDetail(String shopId);

    /**
     * method that allows deletion of shop images
     *
     * @param images a list of images name that user wishes to delete
     * @param shopId shop internal id
     */
    void delete(String[] images, String shopId);
}
