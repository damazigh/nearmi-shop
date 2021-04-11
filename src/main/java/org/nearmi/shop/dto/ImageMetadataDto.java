package org.nearmi.shop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageMetadataDto {
    private String name;
    private boolean root;
    private LocalDateTime uploadedAt;
    private float height;
    private float width;
}
