package org.nearmi.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.nearmi.core.dto.AddressDto;
import org.nearmi.core.mongo.document.shopping.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mappings({
            @Mapping(source = "location.x", target = "longitude"),
            @Mapping(source = "location.y", target = "latitude")
    })
    AddressDto map(Address address);
}
