package org.nearmi.shop.dto;

import lombok.Data;
import org.nearmi.core.dto.AddressDto;

import java.time.LocalDateTime;

@Data
public class ShopSummaryDto {
    private String id;
    private String registrationNumber;
    private AddressDto address;
    private LocalDateTime created;
    private String shortDescription;
}
