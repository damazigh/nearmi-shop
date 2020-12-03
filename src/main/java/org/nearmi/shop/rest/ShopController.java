package org.nearmi.shop.rest;

import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * expose webservice for shopping management and administration
 * @author adjebarri
 * @since  0.1.0
 */
@RestController
public class ShopController {
    @Autowired
    private IShopService shopService;
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ShopDto shop) {
        shopService.create(shop);
        return ResponseEntity.status(201).build();
    }
}
