package org.nearmi.shop.rest.v1;

import org.nearmi.shop.service.IProProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pro/unicity")
public class UnicityController {
    @Autowired
    private IProProductService productService;

    @GetMapping(value = "/{shopId}/product", produces = "application/json")
    public ResponseEntity<Boolean> productUnicity(@PathVariable("shopId") String shopId,
                                                  @RequestParam("field") String field, @RequestParam("value") String value) {
        return ResponseEntity.ok(productService.isUnique(field, value, shopId));
    }
}
