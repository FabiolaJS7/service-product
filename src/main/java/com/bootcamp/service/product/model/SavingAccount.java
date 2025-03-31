package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;

public class SavingAccount extends PassiveProduct {

    @Override
    public double commissionMovement() {
        return 0;
    }

    @Override
    public boolean isFreeCommission() {
        return true;
    }

    @Override
    public String maxMovementPerMonth() {
        return TypeMovementConstants.LIMIT_MAX_PER_MONTH_SAVING;
    }

    @Override
    public String productType() {
        return ProductTypeConstants.SAVING_ACCOUNT;
    }
}
