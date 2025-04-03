package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.constants.TypeMovementConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedAccount extends PassiveProduct {

    public FixedAccount() {
        this.setProductType(ProductTypeConstants.FIXED_ACCOUNT); // Inicializa el atributo productType
        this.setFreeCommission(true);
        this.setCommission(TypeMovementConstants.ZERO_COMMISSION_PER_MOVEMENT);
        this.setMaxMovementPerMonth(TypeMovementConstants.LIMIT_MAX_PER_MONTH_FIXED);
    }


}
