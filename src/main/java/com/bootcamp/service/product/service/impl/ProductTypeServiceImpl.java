package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.mapper.ProductTypeMapper;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.repository.DaoProductTypeFactory;
import com.bootcamp.service.product.service.ProductTypeService;
import com.bootcamp.service.product.util.JsonTransferUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    DaoProductTypeFactory daoProductTypeFactory;


    @Override
    public Mono<ProductTypeResponse> createProductType(Mono<ProductTypeRequest> createProductTypeRequest) {
        return createProductTypeRequest
                .doOnSubscribe(subscription -> log.info("Creating product type"))
                .map(ProductTypeMapper.INSTANCE::getProductTypeOfCreateProductTypeRequest)
                .flatMap(p -> daoProductTypeFactory.getProductTypeDAO().save(p))
                .doOnNext(productType1 -> log.info("Getting product type id and creating product type rs"))
                .map(ProductTypeMapper.INSTANCE::getCreateProductTypeResponseOfProductType)
                .doOnSuccess(createProductTypeResponse -> log.info("Created product type rs: {}",
                        JsonTransferUtil.objectToJson(createProductTypeResponse)))
                .switchIfEmpty(Mono.just(new ProductTypeResponse()))
                .doOnError(throwable -> log.error("Error while creating product type", throwable));

    }

    @Override
    public Mono<ProductTypeResponse> findProductTypeByCode(String productTypeCode) {
        return daoProductTypeFactory.getProductTypeDAO().findProductTypeByCode(productTypeCode)
                .doOnSubscribe(subscription -> log.info("Searching product type by code {}", productTypeCode))
                .map(ProductTypeMapper.INSTANCE::getCreateProductTypeResponseOfProductType)
                .doOnNext(productTypeResponse -> log.info("Product type found and creating product type rs."))
                .doOnSuccess(productTypeResponse -> log.info("Product type found: {}", productTypeResponse))
                .switchIfEmpty(Mono.just(new ProductTypeResponse()))
                .doOnError(throwable -> log.error("Error while searching product type", throwable));
    }
}
