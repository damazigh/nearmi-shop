package org.nearmi.shop.rest.v1;

import org.nearmi.shop.dto.ShopDto;
import org.nearmi.shop.dto.ShopSummaryDto;
import org.nearmi.shop.mapper.ShopMapper;
import org.nearmi.shop.mapper.ShopSummaryMapper;
import org.nearmi.shop.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * expose shop webservice for managing shop
 * all webservice exposed with this class needs <b>Professional profile</b>
 *
 * @author A.djebarri
 * @since 0.1.0
 */

@RestController
@RequestMapping("/v1/pro")
@CrossOrigin(origins = {"http://localhost:3000"}, methods = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class ProShopController {
    @Autowired
    private IShopService shopService;
    @Autowired
    private ShopSummaryMapper shopSummaryMapper;
    @Autowired
    private ShopMapper shopMapper;

    /*
    shop resources
     */
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ShopDto shop) {
        shopService.create(shop);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<Collection<ShopSummaryDto>> userShops() {
        return ResponseEntity.ok(shopSummaryMapper.mapAll(shopService.getBelongingShop()));
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<ShopDto> shopDetail(@PathVariable("shopId") String shopId) {
        return ResponseEntity.ok(shopMapper.map(shopService.getDetail(shopId), null));
    }

    /*
      Shop upload resource
     */
    @PutMapping("/upload/{shopId}")
    public ResponseEntity<Void> upload(@RequestPart("images") MultipartFile[] files, @PathVariable("shopId") String shopId, @RequestParam(value = "root", required = false) String root) {
        shopService.updateImages(files, shopId, root);
        return ResponseEntity.status(HttpStatus.CREATED.value()).build();
    }

    @PutMapping("/delete/{shopId}")
    public ResponseEntity<Void> delete(@RequestParam("images") String[] images, @PathVariable("shopId") String shopId) {
        shopService.delete(images, shopId);
        return ResponseEntity.noContent().build();
    }
}
