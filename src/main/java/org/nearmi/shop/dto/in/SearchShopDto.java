package org.nearmi.shop.dto.in;

import lombok.Data;
import org.nearmi.core.dto.AddressDto;

@Data
public class SearchShopDto {
    private String category;
    private AddressDto address;
}
