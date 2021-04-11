package org.nearmi.shop.repository.custom.impl;

import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.shop.repository.custom.ProductCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ProductRepositoryImpl implements ProductCustomRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean isUnique(String value, String field, String shopId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productOwner.id").is(shopId))
                .addCriteria(Criteria.where(field).is(value));
        return !mongoTemplate.exists(query, Product.class);
    }
}
