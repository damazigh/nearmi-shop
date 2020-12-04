package org.nearmi.shop.mapper;

import org.mapstruct.Mapper;
import org.nearmi.core.dto.AddressDto;
import org.nearmi.core.mongo.document.shopping.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto map(Address address);
}
