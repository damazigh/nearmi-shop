package org.nearmi.shop.repository.custom;

import org.nearmi.core.mongo.document.shopping.Address;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * @author adjebarri
 * @since 0.1.0
 */
public interface ShopCustomRepository {
    /**
     * search shop which reference an address in the given parameters
     *
     * @param addresses addresses id that match a certain perimeter (see {@code nearmi.config.max-distance}
     */
    Page<Shop> findByAddressesIds(Collection<Address> addresses, Pageable pageable);
}
