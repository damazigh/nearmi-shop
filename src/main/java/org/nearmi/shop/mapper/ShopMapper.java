package org.nearmi.shop.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
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
            @Mapping(source = "options.schedulingAppointment", target = "schedulingAppointment"),
            @Mapping(source = "imageMetadata", target = "hasImage", qualifiedByName = "hasImageQualifier")
    })
    ShopDto map(Shop shop);

    Collection<ShopDto> mapAll(Collection<Shop> shops);

    @Named("hasImageQualifier")
    default boolean hasImageQualifier(String metadata) {
        return StringUtils.isNotBlank(metadata);
    }
}
