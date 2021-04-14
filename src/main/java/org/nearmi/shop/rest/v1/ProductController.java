package org.nearmi.shop.rest.v1;

import org.nearmi.core.dto.technical.PaginatedSearchResult;
import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.core.util.HttpUtils;
import org.nearmi.shop.dto.ProductDto;
import org.nearmi.shop.mapper.ProductMapper;
import org.nearmi.shop.service.IProProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
}
