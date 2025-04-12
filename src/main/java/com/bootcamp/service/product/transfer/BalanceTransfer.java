package com.bootcamp.service.product.transfer;

import com.bootcamp.service.product.constants.MovementTypeConstants;
import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BalanceTransfer {
    public Mono<BalanceBeanResponse> getBalanceOfProduct(Product product) {

        return Mono.just(new BalanceBeanResponse());
    }

    public Mono<Product> updateBalance(Product product, BalanceBeanRequest balanceBeanRequest) {
        log.info("Update balance of product {}", product.getId());

        /***   if (ProductTypeConstants.PASSIVE_PRODUCTS.contains(product.getProductType())) {
            BalanceAnt balanceAntFound = product.getDetailsProduct().getPassiveProduct().getBalanceAnt();
            //PassiveProduct passiveProduct = product.getDetailsProduct().getPassiveProduct();

          if (passiveProduct.isFreeCommission() || (Boolean.FALSE.equals(passiveProduct.isFreeCommission())
                    && passiveProduct.getTransactionDone() < Integer.parseInt(passiveProduct.getMaxMovementPerMonth()))) {
                if (balanceBeanRequest.getMovementType().equals(MovementTypeConstants.DEPOSIT)) {
                    balanceFound.setTotalAmount(balanceFound.getTotalAmount() + balanceBeanRequest.getAmount());
                } else if (balanceBeanRequest.getMovementType().equals(MovementTypeConstants.WITHDRAW)) {
                    balanceFound.setTotalAmount(balanceFound.getTotalAmount() - balanceBeanRequest.getAmount());
                }
            }

             product.getDetailsProduct().getPassiveProduct().setBalanceAnt(balanceAntFound);
            return Mono.just(product);
        } else {
            ActiveProduct activeProduct = product.getDetailsProduct().getActiveProduct();

            if (balanceBeanRequest.getMovementType().equals(MovementTypeConstants.DEPOSIT)) {
                activeProduct.setCreditLimitUsed(activeProduct.getCreditLimitUsed() - balanceBeanRequest.getAmount());
                activeProduct.setCreditBalance(activeProduct.getCreditBalance() + balanceBeanRequest.getAmount());
            } else {
                if (activeProduct.isHasCreditCard()) {
                    activeProduct.setCreditLimitUsed(activeProduct.getCreditLimitUsed() + balanceBeanRequest.getAmount());
                    activeProduct.setCreditBalance(activeProduct.getCreditBalance() - balanceBeanRequest.getAmount());
                }

            }
            return Mono.just(product); ***/
            return Mono.just(product);

    }

}
