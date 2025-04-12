package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    private Double creditLimit;
    private Double creditLimitUsed;
    private Double creditEnabledToUse;
    private Double totalAmountInAccount;

}
