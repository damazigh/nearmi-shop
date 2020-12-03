package org.nearmi.shop.rest;

import org.nearmi.shop.dto.ShopDto;
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

    @PostMapping("/{shop_id}/update")
    public void create(@PathVariable("shop_id") String shopId, @RequestBody ShopDto shop) {

    }
}
