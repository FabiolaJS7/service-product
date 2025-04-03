package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.model.Product;
import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.repository.ProductRepository;
import com.bootcamp.service.product.service.BusinessManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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


    @Disabled
    @Test
    void testCreateProduct() {
        //Arrage , preparamos el escensario, los objetos simulados
        ProductRequest productRequest = getProductRequestMock();
        Product productDto = getProductMock();

        Mono<ProductRequest> productRequestMono = Mono.just(productRequest);

        Mockito.when(productRepository.save(productDto)).thenReturn(Mono.just(productDto));
        Mockito.when(businessManager.createProduct(productRequest)).thenReturn(productDto);
        //Act - llamamos al metodo que estamos probando
        final Mono<String> result = productService.createProduct(productRequestMono);

        //Assert para verificar la informacion que espero de la prueba
        Assertions.assertEquals(productDto.getId(), result.block());
        Mockito.verify(productRepository, Mockito.times(1)).save(productDto); //cuántas veces se desea que se llame al save

    }


    @Disabled
    @Test
    void testCreateProduct_shouldReturn404WhenSaveFails() {
        // Arrange
        ProductRequest productRequest = getProductRequestMock();
        Product productDto = getProductMock();

        Mono<ProductRequest> productRequestMono = Mono.just(productRequest);

        // Simula que el repositorio lanza un error al intentar guardar
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mockito.when(businessManager.createProduct(productRequest)).thenReturn(productDto);

        // Act
        Mono<ResponseEntity<String>> result = productService.createProduct(productRequestMono)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body("Product creation failed")));

        // Assert
        ResponseEntity<String> response = result.block();
        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertEquals("Product creation failed", response.getBody());
    }

    private ProductRequest getProductRequestMock() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductType("SA");
        return productRequest;
    }

    private Product getProductMock() {
        Product productDto = new Product();
        productDto.setId("ID-PRUEBA");
        return productDto;
    }
}