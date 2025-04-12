package com.bootcamp.service.product.transfer;

import com.bootcamp.service.product.constants.MovementTypeConstants;
import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BalanceTransfer {
    public Mono<BalanceBeanResponse> getBalanceOfProduct(Product product) {
        BalanceBeanResponse balanceBeanResponse = new BalanceBeanResponse();
        balanceBeanResponse.setCreditLimit(product.getBalance().getCreditLimit());
        balanceBeanResponse.setCreditLimitUsed(product.getBalance().getCreditLimitUsed());
        balanceBeanResponse.setCreditEnabledToUse(product.getBalance().getCreditEnabledToUse());
        balanceBeanResponse.setTotalAmountInAccount(product.getBalance().getTotalAmountInAccount());
        return Mono.just(balanceBeanResponse);
    }

    public Mono<Product> updateBalance(Product product, BalanceBeanRequest balanceBeanRequest) {
        Balance balanceAntFound = product.getBalance();

        if (ProductTypeConstants.PASSIVE_PRODUCTS.contains(product.getProductType())) {
            balanceAntFound.setTotalAmountInAccount(balanceBeanRequest.getMovementType()
                    .equalsIgnoreCase(MovementTypeConstants.DEPOSIT)
                    ? (balanceAntFound.getTotalAmountInAccount() + balanceBeanRequest.getAmount())
                    : (balanceAntFound.getTotalAmountInAccount() - balanceBeanRequest.getAmount()));
        } else {
            if (balanceBeanRequest.getMovementType().equals(MovementTypeConstants.PAYMENT)) {
                balanceAntFound.setCreditLimitUsed(balanceAntFound.getCreditLimitUsed() - balanceBeanRequest.getAmount());
                balanceAntFound.setCreditEnabledToUse(balanceAntFound.getCreditEnabledToUse() + balanceBeanRequest.getAmount());
            } else if (balanceBeanRequest.getMovementType().equals(MovementTypeConstants.CONSUME)) {
                if (product.getProductType().equalsIgnoreCase(ProductTypeConstants.CREDIT_CARD)) {
                    balanceAntFound.setCreditLimitUsed(balanceAntFound.getCreditLimitUsed() + balanceBeanRequest.getAmount());
                    balanceAntFound.setCreditEnabledToUse(balanceAntFound.getCreditEnabledToUse() - balanceBeanRequest.getAmount());
                }
            }
        }
        log.info("Balance updated for to save in product {}", JsonTransferUtil.objectToJson(balanceAntFound));
        return Mono.just(product);

    }
}
