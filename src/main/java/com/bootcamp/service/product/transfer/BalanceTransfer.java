package com.bootcamp.service.product.transfer;

import com.bootcamp.service.product.constants.MovementTypeConstants;
import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.model.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BalanceTransfer {
    public Mono<BalanceBeanResponse> getBalanceOfProduct(Product product) {

        BalanceBeanResponse balanceBeanResponse = new BalanceBeanResponse();
        if (ProductTypeConstants.PASSIVE_PRODUCTS.contains(product.getProductType())) {
            Balance balanceFound = product.getDetailsProduct().getPassiveProduct().getBalance();
            balanceBeanResponse.setBalanceAmount(balanceFound.getTotalAmount());
            return Mono.just(balanceBeanResponse);
        } else {
            ActiveProduct activeProduct = product.getDetailsProduct().getActiveProduct();
            balanceBeanResponse.setCreditLimit(activeProduct.getCreditLimit());
            balanceBeanResponse.setCreditLimitUsed(activeProduct.getCreditLimitUsed());
            balanceBeanResponse.setBalanceAmount(activeProduct.getCreditLimit() - activeProduct.getCreditLimitUsed());
            return Mono.just(balanceBeanResponse);
        }
    }

    public Mono<Product> updateBalance(Product product, BalanceBeanRequest balanceBeanRequest) {
        if (ProductTypeConstants.PASSIVE_PRODUCTS.contains(product.getProductType())) {
            Balance balanceFound = product.getDetailsProduct().getPassiveProduct().getBalance();

            if (balanceBeanRequest.getMovementType().equals(MovementTypeConstants.DEPOSIT)) {
                balanceFound.setTotalAmount(balanceFound.getTotalAmount() + balanceBeanRequest.getAmount());
            } else if (balanceBeanRequest.getMovementType().equals(MovementTypeConstants.WITHDRAW)) {
                balanceFound.setTotalAmount(balanceFound.getTotalAmount() - balanceBeanRequest.getAmount());
            }
             product.getDetailsProduct().getPassiveProduct().setBalance(balanceFound);
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
            return Mono.just(product);
        }
    }

}
