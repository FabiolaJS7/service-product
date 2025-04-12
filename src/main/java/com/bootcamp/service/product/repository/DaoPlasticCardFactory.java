package com.bootcamp.service.product.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DaoPlasticCardFactory {

    private final PlasticCardRepository plateCardRepository;

    public PlasticCardRepository getPlasticCardRepository() {
        return plateCardRepository;
    }
}
