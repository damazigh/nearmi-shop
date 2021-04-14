package org.nearmi.shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private double price;
    private ProductCategoryDto productCategory;
    private int amount;
    private List<ImageMetadataDto> metadata;
}
