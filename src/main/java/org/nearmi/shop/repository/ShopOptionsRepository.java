package org.nearmi.shop.repository;

import org.bson.types.ObjectId;
import org.nearmi.core.mongo.document.shopping.ShopOptions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopOptionsRepository extends MongoRepository<ShopOptions, ObjectId> {
}
