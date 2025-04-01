package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class PassiveProduct extends Product {

    private double commissionMovement;
    private boolean isFreeCommission;
    private String maxMovementPerMonth;



    


}
