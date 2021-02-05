package org.nearmi.shop.dto.in;

import lombok.Data;

@Data
public class CreateProductDto {
    private String name;
    private String description;
    private double price;
    private int initialAmount;
    private String productCategory;
}
