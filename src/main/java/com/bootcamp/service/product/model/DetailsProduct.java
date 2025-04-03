package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsProduct {

    private String familyProduct;
    private PassiveProduct passiveProduct;
    private ActiveProduct activeProduct;
}
