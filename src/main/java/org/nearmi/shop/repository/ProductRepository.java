package org.nearmi.shop.repository;

import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.shop.repository.custom.ProductCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author adjebarri
 * @since 0.1.0
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String>, ProductCustomRepository {
    /**
     * find a page of product belong to shop with id {@code shopId} and
     * fitting pagination parameter hold in {@code pageable} param
     *
     * @param shopId   targeted shop
     * @param pageable pagination parameter
     * @return {@link Page} of {@link Product}
     */
    Page<Product> findByProductOwnerId(String shopId, Pageable pageable);
}
