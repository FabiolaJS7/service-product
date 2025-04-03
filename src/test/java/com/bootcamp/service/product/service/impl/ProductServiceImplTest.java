package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.model.Product;
import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.repository.ProductRepository;
import com.bootcamp.service.product.service.BusinessManager;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    BusinessManager businessManager;


    @Test
    void createProductInformation() {
        ProductRequest productRequest = getProductRequestMock();
        Product productDto = getProductMock();

        Mono<ProductRequest> productRequestMono = Mono.just(productRequest);

        Mockito.when(productRepository.save(productDto)).thenReturn(Mono.just(productDto));
        Mockito.when(businessManager.createProduct(productRequest)).thenReturn(productDto);

        final Mono<String> result = productService.createProductInformation(productRequestMono);

        //Assert
        Assertions.assertEquals(productDto.getId(), result.block());
        Mockito.verify(productRepository, Mockito.times(1)).save(productDto); //cuántas veces se desea que se llame al save

    }


    @Test
    void createProductInformation_shouldReturn404WhenSaveFails() {
        // Arrange
        ProductRequest productRequest = getProductRequestMock();
        Product productDto = getProductMock();

        Mono<ProductRequest> productRequestMono = Mono.just(productRequest);

        // Simula que el repositorio lanza un error al intentar guardar
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mockito.when(businessManager.createProduct(productRequest)).thenReturn(productDto);

        // Act
        Mono<ResponseEntity<String>> result = productService.createProductInformation(productRequestMono)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body("Product creation failed")));

        // Assert
        ResponseEntity<String> response = result.block();
        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertEquals("Product creation failed", response.getBody());
    }

    private ProductRequest getProductRequestMock() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setCustomerId("1111111");
        productRequest.setCustomerType("P");
        productRequest.setProductType("SA");
        productRequest.setUserBank("admin");
        return productRequest;
    }

    private Product getProductMock() {
        Product productDto = new Product();
        productDto.setId("ID-PRUEBA");
        return productDto;
    }
}