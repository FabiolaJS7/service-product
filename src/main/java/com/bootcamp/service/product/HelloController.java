package com.bootcamp.service.product;

import jdk.jfr.Registered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api-v2/products")
public class HelloController {

    @GetMapping
    public Mono<ResponseEntity<Flux<String>>> getProducts () {
        Flux<String> products = Flux.just("Apple", "Banana", "Orange");
        products.subscribe(System.out::println);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(products));
    }
}
