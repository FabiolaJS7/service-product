package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentAccount extends PassiveProduct {

    public CurrentAccount() {
        this.setProductType(ProductTypeConstants.CURRENT_ACCOUNT); // Inicializa el atributo productType
        this.setFreeCommission(false);
        this.setCommission(TypeMovementConstants.AMOUNT_COMMISSION_PER_MOVEMENT);
        this.setMaxMovementPerMonth(TypeMovementConstants.FREE_MOVEMENT);
    }


}
