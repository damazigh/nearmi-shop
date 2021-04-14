package org.nearmi.shop.dto.in;

import lombok.Data;

import java.util.List;

@Data
public class CreateProductDto {
    private String name;
    private String description;
    private double price;
    private Integer quantity;
    private String productCategory;
    private List<ImageBoundariesDto> boundaries;
}
