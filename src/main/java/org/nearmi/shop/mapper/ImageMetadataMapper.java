package org.nearmi.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.nearmi.core.mongo.document.technical.ImageMetadata;
import org.nearmi.shop.dto.ImageMetadataDto;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ImageMetadataMapper {
    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "rootImage", target = "root"),
            @Mapping(source = "uploadedAt", target = "uploadedAt")
    })
    ImageMetadataDto map(ImageMetadata metadata);

    Collection<ImageMetadataDto> mapAll(Collection<ImageMetadata> imagesMetadata);
}
