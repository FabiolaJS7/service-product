package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;


public class CurrentAccount extends PassiveProduct {
    @Override
    public String getProductType() {
        return ProductTypeConstants.CURRENT_ACCOUNT;
    }

    @Override
    public boolean isFreeCommission() {
        return false;
    }

    @Override
    public String getMaxMovementPerMonth() {
        return TypeMovementConstants.FREE_MOVEMENT;
    }

    @Override
    public double getCommissionMovement() {
        return 10.00;
    }
}
