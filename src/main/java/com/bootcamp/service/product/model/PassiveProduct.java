package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PassiveProduct {

    private String productType;
    private boolean isFreeCommission;
    private double amountOfOpen;
    private String accountNumber;
    private InfoTransaction infoTransaction;
    private Balance balance;





    


}
