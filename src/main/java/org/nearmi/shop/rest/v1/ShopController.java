package org.nearmi.shop.rest.v1;

import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.util.HttpUtils;
import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.dto.in.SearchShopDto;
import org.nearmi.shop.mapper.ShopMapper;
import org.nearmi.shop.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static org.nearmi.core.util.HttpUtils.parsePaginationParam;

/**
 * expose webservice for shops basic operation (search nearby shop, get shop image)
 *
 * @author adjebarri
 * @since 0.1.0
 */
@RestController
@RequestMapping("/v1")
public class ShopController {
    @Autowired
    private IShopService shopService;
    @Autowired
    private ShopMapper shopMapper;

    @PostMapping("/search")
    public ResponseEntity<Collection<ShopDto>> search(@RequestParam(required = false) String limit,
                                                      @RequestParam(required = false) String offset,
                                                      @RequestBody SearchShopDto searchShop,
                                                      HttpServletResponse res) {
        PaginatedSearchResult<Shop> psr = shopService.search(searchShop,
                PageRequest.of(parsePaginationParam(offset, 0), parsePaginationParam(limit, 1)));
        HttpUtils.addPaginationHeader(res, psr);
        return ResponseEntity.status(psr.whichStatus()).body(shopMapper.mapAll(psr.getContent(), searchShop.getAddress()));
    }

    @GetMapping(value = "/{shopId}/image/{name}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] loadShopImage(@PathVariable("shopId") String shopId, @PathVariable("name") String name) {
        return shopService.loadImage(shopId, name);
    }


}
