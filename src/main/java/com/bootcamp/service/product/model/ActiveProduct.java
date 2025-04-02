package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.StatusProductConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveProduct extends Product {

    private boolean hasCreditCard;
    private double creditLimit = 3000.00;
    private double creditLimitUsed = 0.0;
    private CreditCard creditCard;

    public double getCreditLimitEnabled() {
        return creditLimit - creditLimitUsed;
    }


}
