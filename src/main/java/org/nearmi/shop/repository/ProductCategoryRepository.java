package org.nearmi.shop.repository;

import org.nearmi.core.mongo.document.shopping.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {
}
