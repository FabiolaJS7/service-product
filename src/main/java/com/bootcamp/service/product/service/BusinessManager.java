package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.Product;
import com.bootcamp.service.product.model.ProductRequest;

public interface BusinessManager {
    Product createProduct(ProductRequest prq);
}
