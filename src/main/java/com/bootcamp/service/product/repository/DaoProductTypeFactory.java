package com.bootcamp.service.product.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DaoProductTypeFactory {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeRepository getProductTypeDAO() {
        return productTypeRepository;
    }

}
