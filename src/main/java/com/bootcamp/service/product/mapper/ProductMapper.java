package com.bootcamp.service.product.mapper;

import com.bootcamp.service.product.model.Product;
import com.bootcamp.service.product.model.ProductRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product getProductOfProductRequest(ProductRequest productRequest) {
        Product product = new Product();
        product.setId(productRequest.getId());
        return null;
    }
}
