package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.CreateProductTypeRequest;
import com.bootcamp.service.product.model.CreateProductTypeResponse;
import com.bootcamp.service.product.model.ProductType;
import reactor.core.publisher.Mono;

public interface ProductTypeService {

    Mono<CreateProductTypeResponse> createProductType(Mono<CreateProductTypeRequest> createProductTypeRequest);
    Mono<ProductType> findProductTypeByCode(String productTypeCode);
}
