package org.nearmi.shop.rest.v1;

import org.nearmi.shop.dto.ProductCategoryDto;
import org.nearmi.shop.dto.in.CreateProductCategoryDto;
import org.nearmi.shop.mapper.ProductCategoryMapper;
import org.nearmi.shop.service.IProProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/v1/pro/{shopId}/product/category")
public class ProProductCategoryController {
    @Autowired
    private IProProductCategoryService proProductCategoryService;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Void> createProductCategory(@PathVariable("shopId") String shopId, @RequestBody Collection<CreateProductCategoryDto> productCategoryDto) {
        proProductCategoryService.createProductCategories(shopId, productCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Collection<ProductCategoryDto>> listProductCategory(@PathVariable("shopId") String shopId) {
        return ResponseEntity.ok(productCategoryMapper.mapAll(proProductCategoryService.listProductCategory(shopId)));
    }

    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable("shopId") String shopId, @PathVariable("categoryId") String categoryId) {
        proProductCategoryService.deleteProductCategory(shopId, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
