package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingAccount extends PassiveProduct {

   public SavingAccount() {
        this.setProductType(ProductTypeConstants.SAVING_ACCOUNT); // Establece el productType como "SA"
        this.setFreeCommission(true);
        this.setMaxMovementPerMonth(TypeMovementConstants.LIMIT_MAX_PER_MONTH_SAVING);
        this.setCommissionMovement(0.00);
    }

}
