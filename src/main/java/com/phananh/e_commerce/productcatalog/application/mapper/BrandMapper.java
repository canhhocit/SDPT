package com.phananh.e_commerce.productcatalog.application.mapper;

import com.phananh.e_commerce.productcatalog.application.dto.query.BrandSearchQuery;
import com.phananh.e_commerce.productcatalog.application.dto.response.BrandResponse;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "brandId", source = "id")
    @Mapping(target = "brandName", source = "name")
    @Mapping(target = "brandDescription", source = "description")
    @Mapping(target = "brandImage", source = "imageUrl")
    @Mapping(target = "isEnabled", source = "isEnabled")
    BrandResponse toResponse(Brand brand);
}


