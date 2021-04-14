package org.nearmi.shop.mapper;

import org.mapstruct.Mapper;
import org.nearmi.core.mongo.document.shopping.Product;
import org.nearmi.shop.dto.ProductDto;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ImageMetadataMapper.class, ProductCategoryMapper.class})
public interface ProductMapper {

    ProductDto map(Product product);

    Collection<ProductDto> mapAll(Collection<Product> products);
}
