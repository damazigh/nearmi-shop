package org.nearmi.shop.repository;

import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.shop.repository.custom.ProductCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    /**
     * find a product by it's id and product owner id
     *
     * @param id             of the product
     * @param productOwnerId shop that owns the product
     * @return the product if found else @{code {@link Optional#empty()}}
     */
    Optional<Product> findByIdAndProductOwnerId(String id, String productOwnerId);

    Optional<Product> findByNameAndProductOwnerId(String name, String productOwnerId);
}
