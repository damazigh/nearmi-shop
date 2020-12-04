package org.nearmi.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.shop.dto.ShopDto;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface ShopMapper {

    @Mappings({
            @Mapping(source = "category.label", target = "category"),
            @Mapping(source = "options.opensAt", target = "opensAt"),
            @Mapping(source = "options.closesAt", target = "closesAt"),
            @Mapping(source = "options.breakClosureStart", target = "breakClosureStart"),
            @Mapping(source = "options.breakClosureEnd", target = "breakClosureEnd"),
            @Mapping(source = "options.openWithoutClosure", target = "withoutBreakClosure"),
            @Mapping(source = "options.schedulingAppointment", target = "schedulingAppointment")
    })
    ShopDto map(Shop shop);
    
    Collection<ShopDto> mapAll(Collection<Shop> shops);
}
