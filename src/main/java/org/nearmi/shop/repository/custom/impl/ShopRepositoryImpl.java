package org.nearmi.shop.repository.custom.impl;

import org.nearmi.core.mongo.document.shopping.Address;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.shop.repository.custom.ShopCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author adjebarri
 * @since 0.1.0
 */
public class ShopRepositoryImpl implements ShopCustomRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private Environment env;

    @Override
    public Page<Shop> findByAddressesIds(Collection<Address> addresses, Pageable pageable) {

        Query q = new Query();
        Query cq = new Query();

        Criteria criteria = Criteria.where("address.$id").in(addresses.stream().map(Address::getId).collect(Collectors.toList()));
        q.addCriteria(criteria);
        cq.addCriteria(criteria);
        long total = mongoTemplate.count(cq, "shop");
        q.with(pageable);
        List<Shop> c = mongoTemplate.find(q, Shop.class);
        return new PageImpl<Shop>(c, pageable, total);
    }
}
