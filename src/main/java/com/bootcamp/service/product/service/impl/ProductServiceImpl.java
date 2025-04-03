package com.bootcamp.service.product.service.impl;


import com.bootcamp.service.product.mapper.ProductMapper;
import com.bootcamp.service.product.model.Product;
import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.repository.ProductRepository;
import com.bootcamp.service.product.service.BusinessManager;
import com.bootcamp.service.product.service.ProductService;
import com.bootcamp.service.product.util.JsonTransferUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    BusinessManager businessManager;

    @Override
    public Mono<String> createProductInformation(Mono<ProductRequest> productRequest) {
        return productRequest.flatMap(prq -> productRepository.save(businessManager.createProduct(prq)))
                .doOnNext(product -> log.info(JsonTransferUtil.objectToJson(product)))
                .map(Product::getId)
                .doOnError(e -> log.error("Error creating product: {}", e.getMessage(), e));

    }
}
