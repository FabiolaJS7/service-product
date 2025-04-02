package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoTransaction {

    private double commission;
    private String maxPerMonth;
    private int transactionDone;
    private boolean enabledToMovement;
}
