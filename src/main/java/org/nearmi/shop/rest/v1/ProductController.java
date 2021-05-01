package org.nearmi.shop.rest.v1;

import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.core.util.HttpUtils;
import org.nearmi.shop.dto.ProductDto;
import org.nearmi.shop.mapper.ProductMapper;
import org.nearmi.shop.service.IProProductService;
import org.nearmi.shop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static org.nearmi.core.util.HttpUtils.parsePaginationParam;

@RestController
@RequestMapping("/v1/{shopId}/product")
public class ProductController {
    @Autowired
    private IProProductService proProductService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private IProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProductDto>> getProduct(@PathVariable("shopId") String shopId,
                                                             @RequestParam(value = "offset", required = false) String offset,
                                                             @RequestParam(value = "limit", required = false) String limit,
                                                             HttpServletResponse response
    ) {
        PaginatedSearchResult<Product> psr = proProductService.getProducts(shopId, PageRequest.of(
                parsePaginationParam(offset, 0),
                parsePaginationParam(limit, 1)));
        HttpUtils.addPaginationHeader(response, psr);
        return ResponseEntity.status(psr.whichStatus()).body(productMapper.mapAll(psr.getContent()));
    }

    @GetMapping("/{productName}")
    public ResponseEntity<ProductDto> getProductByName(@PathVariable("shopId") String shopId, @PathVariable("productName") String productName) {
        return ResponseEntity.ok(productMapper.map(productService.getProductByName(shopId, productName)));
    }

    @GetMapping(value = "/{productName}/image/{imageName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> loadProductImage(@PathVariable("shopId") String shopId,
                                                   @PathVariable("productName") String productName,
                                                   @PathVariable("imageName") String imageName) {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Cache-Control", "max-age=3000")
                .body(productService.loadProductImage(shopId, productName, imageName));
    }
}
