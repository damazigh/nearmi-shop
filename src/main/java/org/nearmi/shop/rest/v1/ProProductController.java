package org.nearmi.shop.rest.v1;

import org.nearmi.shop.dto.in.CreateProductDto;
import org.nearmi.shop.mapper.ProductMapper;
import org.nearmi.shop.service.IProProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/pro/{shopId}/product")
public class ProProductController {

    @Autowired
    private IProProductService proProductService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createProduct(@PathVariable("shopId") String shopId,
                                              @RequestPart("images") MultipartFile files[], @RequestPart("data") CreateProductDto data) {
        proProductService.createProduct(shopId, data, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

 
}
