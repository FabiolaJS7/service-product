package com.bootcamp.service.product.constants;

import java.util.HashMap;

public class PrefixPassiveAccountConstants {

    public static final HashMap<String, String> NUMBER_ACCOUNT = new HashMap<>();

    static {
        NUMBER_ACCOUNT.put(ProductTypeConstants.SAVING_ACCOUNT, "SA01-");
        NUMBER_ACCOUNT.put(ProductTypeConstants.CURRENT_ACCOUNT, "CA01-");
        NUMBER_ACCOUNT.put(ProductTypeConstants.FIXED_ACCOUNT, "FA01-");
        NUMBER_ACCOUNT.put(ProductTypeConstants.CREDIT_CARD, "CC01-");
        NUMBER_ACCOUNT.put(ProductTypeConstants.CREDIT_PERSONAL, "CP01-");
        NUMBER_ACCOUNT.put(ProductTypeConstants.CREDIT_BUSINESS, "CB01-");
    }
}
