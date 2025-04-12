package com.bootcamp.service.product.mapper;

import com.bootcamp.service.product.model.CreateProductTypeRequest;
import com.bootcamp.service.product.model.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductTypeMapper {

    ProductTypeMapper INSTANCE = Mappers.getMapper(ProductTypeMapper.class);

    // Mapea ProductType a createProductTypeRequest
    ProductType getProductTypeOfCreateProductTypeRequest(CreateProductTypeRequest createProductTypeRequest);
}
