package org.nearmi.shop.repository;

import org.nearmi.core.mongo.document.shopping.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}