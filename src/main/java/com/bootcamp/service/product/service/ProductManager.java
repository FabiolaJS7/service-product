package com.bootcamp.service.product.service;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.util.AuditDataUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

@Component
public class ProductManager {
    public ProductInformation createNewProduct(ProductRequest prq, ProductInformation productInformation) {
        productInformation.setCustomerId(prq.getCustomerId());

        switch (prq.getProductType()) {
            case ProductTypeConstants.SAVING_ACCOUNT:
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setAccountNumber(UUID.randomUUID().toString());
                saveAdditionalInformation(savingAccount, prq);
                productInformation.getPassiveProduct().add(savingAccount);
                break;
            case ProductTypeConstants.CURRENT_ACCOUNT:
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setAccountNumber(UUID.randomUUID().toString());
                saveAdditionalInformation(currentAccount, prq);
                productInformation.getPassiveProduct().add(currentAccount);
                break;
            case ProductTypeConstants.FIXED_ACCOUNT:
                FixedAccount fixedAccount = new FixedAccount();
                fixedAccount.setAccountNumber(UUID.randomUUID().toString());
                saveAdditionalInformation(fixedAccount, prq);
                productInformation.getPassiveProduct().add(fixedAccount);
                break;
            case ProductTypeConstants.CREDIT_PERSONAL:
                ActiveProduct personalCredit = new ActiveProduct();
                personalCredit.setAccountNumber(UUID.randomUUID().toString());
                personalCredit.setProductType(ProductTypeConstants.CREDIT_PERSONAL);
                saveAdditionalInformation(personalCredit, prq);
                productInformation.getActiveProduct().add(personalCredit);
                break;
            case ProductTypeConstants.CREDIT_BUSINESS:
                ActiveProduct businessCredit = new ActiveProduct();
                businessCredit.setAccountNumber(UUID.randomUUID().toString());
                saveAdditionalInformation(businessCredit, prq);
                businessCredit.setProductType(ProductTypeConstants.CREDIT_BUSINESS);
                productInformation.getActiveProduct().add(businessCredit);
                break;
            default:
                ActiveProduct creditCard = new ActiveProduct();
                creditCard.setAccountNumber(UUID.randomUUID().toString());
                saveAdditionalInformation(creditCard, prq);
                creditCard.setProductType(ProductTypeConstants.CREDIT_CARD);
                productInformation.getActiveProduct().add(creditCard);
                break;
        }



        return productInformation;
    }

    private void saveAdditionalInformation(Product product, ProductRequest prq) {
        product.getHolders().addAll(prq.getHolders()
                .stream()
                .map(holder -> {
                    PersonalInformationAdd personalInformationAdd = new PersonalInformationAdd();
                    personalInformationAdd.setName(holder.getName());
                    personalInformationAdd.setEmail(holder.getEmail());
                    personalInformationAdd.setLastName(holder.getLastName());
                    personalInformationAdd.setPhone(holder.getPhone());
                    return personalInformationAdd;
                })
                .toList());

        product.getAuthorizedSignatories().addAll(prq.getAuthorizedSignatories()
                .stream()
                .map(signatureAuth -> {
                    PersonalInformationAdd personalInformationAdd = new PersonalInformationAdd();
                    personalInformationAdd.setName(signatureAuth.getName());
                    personalInformationAdd.setEmail(signatureAuth.getEmail());
                    personalInformationAdd.setLastName(signatureAuth.getLastName());
                    personalInformationAdd.setPhone(signatureAuth.getPhone());
                    return personalInformationAdd;
                })
                .toList());

        product.setAuditData(AuditDataUtil.create(prq.getUserBank()));
    }
}
