package com.bootcamp.service.product.mapper;

import com.bootcamp.service.product.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductTypeMapper {

    ProductTypeMapper INSTANCE = Mappers.getMapper(ProductTypeMapper.class);

    // Mapea ProductType a createProductTypeRequest
    ProductType getProductTypeOfCreateProductTypeRequest(ProductTypeRequest createProductTypeRequest);

    ProductTypeResponse getCreateProductTypeResponseOfProductType(ProductType productType);
}
