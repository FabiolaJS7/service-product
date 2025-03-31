package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;


public class CurrentAccount extends PassiveProduct {
    @Override
    public double commissionMovement() {
        return 10;
    }

    @Override
    public boolean isFreeCommission() {
        return false;
    }

    @Override
    public String maxMovementPerMonth() {
        return TypeMovementConstants.FREE_MOVEMENT;
    }

    @Override
    public String productType() {
        return ProductTypeConstants.CURRENT_ACCOUNT;
    }
}
