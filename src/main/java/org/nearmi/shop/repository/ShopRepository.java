package org.nearmi.shop.repository;

import org.bson.types.ObjectId;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends MongoRepository<Shop, ObjectId> {

}
