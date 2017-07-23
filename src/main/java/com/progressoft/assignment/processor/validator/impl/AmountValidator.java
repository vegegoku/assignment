package com.progressoft.assignment.processor.validator.impl;

import com.progressoft.assignment.processor.validator.TransactionValidator;

public class AmountValidator implements TransactionValidator{

    public boolean isValid(String transactionType,String amount,String date) {
        return Double.parseDouble(amount) > 0;
    }

}
