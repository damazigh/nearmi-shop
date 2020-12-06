package org.nearmi.shop.repository;

import org.bson.types.ObjectId;
import org.nearmi.core.mongo.document.shopping.Address;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.shop.repository.custom.ShopCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ShopRepository extends MongoRepository<Shop, ObjectId>, ShopCustomRepository {
    Page<Shop> findByAddressIn(Collection<Address> addr, Pageable pageable);
}
