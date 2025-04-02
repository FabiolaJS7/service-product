package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedAccount extends PassiveProduct {

    @Override
    public String getProductType() {
        return ProductTypeConstants.FIXED_ACCOUNT; //When create an instance of this clase, productType is filled
    }

    @Override
    public boolean isFreeCommission() {
        return true;
    }

    @Override
    public void setInfoTransaction(InfoTransaction infoTransaction) {
        infoTransaction.setCommission(TypeMovementConstants.ZERO_COMMISSION_PER_MOVEMENT);
        infoTransaction.setMaxPerMonth(TypeMovementConstants.LIMIT_MAX_PER_MONTH_FIXED);
        super.setInfoTransaction(infoTransaction);
    }

}
