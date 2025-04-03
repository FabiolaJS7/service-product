package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingAccount extends PassiveProduct {

    public SavingAccount() {
        this.setProductType(ProductTypeConstants.SAVING_ACCOUNT); // Inicializa el atributo productType
        this.setFreeCommission(true);
    }


}
