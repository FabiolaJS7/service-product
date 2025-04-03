package com.bootcamp.service.product.transfer;

import com.bootcamp.service.product.model.Product;
import com.bootcamp.service.product.model.ProductResponse;
import com.bootcamp.service.product.util.JsonTransferUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductTransferTest {

    @Autowired
    ProductTransfer productTransfer;


    @Test
    void getProductResponseOfProduct() {
        List<Product> products = Arrays.asList(JsonTransferUtil.getObjectFromJSONFile(Product[].class, "products.json"));
        List<ProductResponse> productResponses = products.stream()
                .map(product -> productTransfer.getProductResponseOfProduct(product))
                .toList();

        assertEquals(productResponses.size(), productResponses.size());
    }
}