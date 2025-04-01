package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;

public class FixedAccount extends PassiveProduct {

    @Override
    public String getProductType() {
        return ProductTypeConstants.FIXED_ACCOUNT;
    }

    @Override
    public boolean isFreeCommission() {
        return true;
    }

    @Override
    public String getMaxMovementPerMonth() {
        return TypeMovementConstants.LIMIT_MAX_PER_MONTH_FIXED;
    }

    @Override
    public double getCommissionMovement() {
        return 0.00;
    }
}
