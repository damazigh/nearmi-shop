package org.nearmi.shop.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.nearmi.core.dto.AddressDto;
import org.nearmi.core.mongo.document.shopping.Shop;
import org.nearmi.core.util.GeoSpatialUtils;
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
            @Mapping(source = "imageMetadata", target = "hasImage", qualifiedByName = "hasImageQualifier"),
    })
    ShopDto map(Shop shop, @Context AddressDto address);

    Collection<ShopDto> mapAll(Collection<Shop> shops, @Context AddressDto address);

    @Named("hasImageQualifier")
    default boolean hasImageQualifier(String metadata) {
        return StringUtils.isNotBlank(metadata);
    }

    @AfterMapping
    default void distance(Shop shop, @MappingTarget ShopDto shopDto, @Context AddressDto address) {
        if (address != null) { // distance is not  needed when managing shops
            shopDto.setDistance(GeoSpatialUtils.haversineDistance(
                    address.getLongitude(), address.getLatitude(),
                    shop.getAddress().getLocation().getX(), shop.getAddress().getLocation().getY()
            ));
        }
    }
}
