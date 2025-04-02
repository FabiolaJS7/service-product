package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentAccount extends PassiveProduct {

    @Override
    public String getProductType() {
        return ProductTypeConstants.CURRENT_ACCOUNT; //When create an instance of this clase, productType is filled
    }

    @Override
    public boolean isFreeCommission() {
        return false;
    }

    @Override
    public void setInfoTransaction(InfoTransaction infoTransaction) {
        infoTransaction.setCommission(TypeMovementConstants.AMOUNT_COMMISSION_PER_MOVEMENT);
        infoTransaction.setMaxPerMonth(TypeMovementConstants.FREE_MOVEMENT);
        super.setInfoTransaction(infoTransaction);
    }

}
