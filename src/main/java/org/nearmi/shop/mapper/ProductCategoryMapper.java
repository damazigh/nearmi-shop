package org.nearmi.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.nearmi.core.mongo.document.shopping.ProductCategory;
import org.nearmi.shop.dto.ProductCategoryDto;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    @Mappings({})
    ProductCategoryDto map(ProductCategory productCategory);

    Collection<ProductCategoryDto> mapAll(Collection<ProductCategory> productCategories);
}
