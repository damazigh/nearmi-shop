package org.nearmi.shop.rest;

import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.util.HttpUtils;
import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.dto.in.SearchShopDto;
import org.nearmi.shop.mapper.ShopMapper;
import org.nearmi.shop.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static org.nearmi.core.util.HttpUtils.parsePaginationParam;

/**
 * expose webservice for shopping management and administration
 *
 * @author adjebarri
 * @since 0.1.0
 */
@RestController
public class ShopController {
    @Autowired
    private IShopService shopService;
    @Autowired
    private ShopMapper shopMapper;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ShopDto shop) {
        shopService.create(shop);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PostMapping("/search")
    public ResponseEntity<Collection<ShopDto>> search(@RequestParam(required = false) String limit,
                                                      @RequestParam(required = false) String offset,
                                                      @RequestBody SearchShopDto searchShop,
                                                      HttpServletResponse res) {

        PaginatedSearchResult<Shop> psr = shopService.search(searchShop,
                PageRequest.of(parsePaginationParam(offset, 0), parsePaginationParam(limit, 1)));
        HttpUtils.addPaginationHeader(res, psr);
        return ResponseEntity.status(psr.whichStatus()).body(shopMapper.mapAll(psr.getContent()));
    }
}
