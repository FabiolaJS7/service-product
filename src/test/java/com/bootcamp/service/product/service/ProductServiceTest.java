package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.util.AuditDataUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    void testCreateProductInformation() {

        //Arrange
        ProductRequest productRequest = this.getProductRequestActive();

        //Act
        Mono<String> productId = productService.createProduct(Mono.just(productRequest));

        // Assert
        StepVerifier.create(productId)
                .expectNextMatches(s -> !s.isEmpty())
                .verifyComplete();

    }

    public ProductRequest getProductRequestActive() {

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductType("SA");
        productRequest.setFamilyProduct("ACTIVE");

        CustomerBean customer = new CustomerBean();
        customer.setCustomerId("45676566");
        customer.setCustomerType("DNI");
        productRequest.setCustomer(customer);

        ActiveProductBean activeProductBean = new ActiveProductBean();
        activeProductBean.hasCreditCard(true);
        activeProductBean.setCreditLimit(30000.00);
        productRequest.setActiveProduct(activeProductBean);

        List<AdditionalPersonBean> additionalPersonBeans = new ArrayList<>();
        AdditionalPersonBean additionalPersonBean = new AdditionalPersonBean();
        additionalPersonBean.setFullName("John Doe");
        additionalPersonBean.setEmail("john.doe@gmail.com");
        additionalPersonBean.setPhone("123456789");
        IdentificationBean identificationBean = new IdentificationBean();
        identificationBean.setNumberIdentification("2123232");
        identificationBean.setTypeIdentification("DNI");
        additionalPersonBean.setIdentification(identificationBean);
        additionalPersonBeans.add(additionalPersonBean);
        productRequest.setHolders(additionalPersonBeans);

        productRequest.setAuthorizedSignatories(additionalPersonBeans);
        productRequest.setUserBank("mo.garcia");
        return productRequest;
    }
}