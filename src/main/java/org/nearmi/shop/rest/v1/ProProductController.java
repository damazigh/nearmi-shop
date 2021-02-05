package org.nearmi.shop.rest.v1;

import org.nearmi.shop.dto.in.CreateProductCategoryDto;
import org.nearmi.shop.dto.in.CreateProductDto;
import org.nearmi.shop.service.IProProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/pro/{shopId}/product")
@CrossOrigin(origins = {"http://localhost:3000"}, methods = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT})
public class ProProductController {

    @Autowired
    private IProProductService proProductService;

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public void createProduct(@PathVariable("shopId") String shopId,
                              @RequestPart MultipartFile files[], @RequestPart("data") CreateProductDto data) {
    }

    @PostMapping("/category/create")
    public ResponseEntity<Void> createProductCategory(@PathVariable("shopId") String shopId, @RequestBody CreateProductCategoryDto productCategoryDto) {
        proProductService.createProductCategory(shopId, productCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
