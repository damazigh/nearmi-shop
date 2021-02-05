package org.nearmi.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.shop.dto.ShopSummaryDto;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface ShopSummaryMapper {
    @Mappings({@Mapping(source = "shortDesc", target = "shortDescription")})
    ShopSummaryDto map(Shop shop);

    Collection<ShopSummaryDto> mapAll(Collection<Shop> shops);
}
