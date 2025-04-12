package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.mapper.ProductTypeMapper;
import com.bootcamp.service.product.model.CreateProductTypeRequest;
import com.bootcamp.service.product.model.CreateProductTypeResponse;
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
    public Mono<CreateProductTypeResponse> createProductType(Mono<CreateProductTypeRequest> createProductTypeRequest) {
        return createProductTypeRequest
                .doOnSubscribe(subscription -> log.info("Creating product type"))
                .map(ProductTypeMapper.INSTANCE::getProductTypeOfCreateProductTypeRequest)
                .flatMap(p -> daoProductTypeFactory.getProductTypeDAO().save(p))
                .doOnNext(productType1 -> log.info("Getting product type id and creating product type rs"))
                .flatMap(p -> {
                    CreateProductTypeResponse createProductTypeResponse = new CreateProductTypeResponse();
                    createProductTypeResponse.setCode(p.getCode());
                    return Mono.just(createProductTypeResponse);
                })
                .doOnSuccess(createProductTypeResponse -> log.debug("Created product type rs: {}",
                        JsonTransferUtil.objectToJson(createProductTypeResponse)))
                .switchIfEmpty(Mono.just(new CreateProductTypeResponse()))
                .doOnError(throwable -> log.error("Error while creating product type", throwable));

    }
}
