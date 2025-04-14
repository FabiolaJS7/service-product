package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.PlasticCardBean;
import com.bootcamp.service.product.model.Product;
import reactor.core.publisher.Mono;

public interface PlasticCardService {

    Mono<String> createPlasticCard(Product product);
    Mono<PlasticCardBean> getPlasticCardById(String cardNumber);
}
