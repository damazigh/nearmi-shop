package org.nearmi.shop.rest.v1;

import org.nearmi.shop.dto.in.CreateProductDto;
import org.nearmi.shop.service.IProProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(consumes = {"multipart/form-data"})
    public void createProduct(@PathVariable("shopId") String shopId,
                              @RequestPart MultipartFile files[], @RequestPart("data") CreateProductDto data) {
    }


}
