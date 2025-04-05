package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveProduct {

    private String productType;
    private boolean hasCreditCard;
    private double creditLimit;
    private double creditLimitUsed;
    private CreditCard creditCard;

}
