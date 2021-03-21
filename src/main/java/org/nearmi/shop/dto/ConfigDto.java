package org.nearmi.shop.dto;

import lombok.Data;

@Data
public class ConfigDto {
    private int maxImageForShop;
    private String[] acceptedImageMime;
}
