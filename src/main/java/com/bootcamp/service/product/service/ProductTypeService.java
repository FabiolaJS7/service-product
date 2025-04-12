package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.*;
import reactor.core.publisher.Mono;

public interface ProductTypeService {

    Mono<ProductTypeResponse> createProductType(Mono<ProductTypeRequest> createProductTypeRequest);
    Mono<ProductTypeResponse> findProductTypeByCode(String productTypeCode);
}
