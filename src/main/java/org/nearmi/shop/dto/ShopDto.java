package org.nearmi.shop.dto;

import lombok.Data;
import org.nearmi.core.dto.AddressDto;
import org.nearmi.core.mongo.document.technical.Time;

@Data
public class ShopDto {
    private String id;
    private String name;
    private String description;
    private String shortDescription;
    private String registrationNumber;
    private AddressDto address;
    private Time opensAt;
    private Time closesAt;
    private String category;
    private boolean withoutBreakClosure;
    private boolean automaticOrderConfirmation;
    private boolean schedulingAppointment;
    private boolean hasImage;
    private Time breakClosureStart;
    private Time breakClosureEnd;
}
