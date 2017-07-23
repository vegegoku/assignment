package com.progressoft.assignment.processor.validator.impl;

import com.progressoft.assignment.processor.validator.TransactionValidator;

import java.util.Date;

public class DateValidator implements TransactionValidator {

    @Override
    public boolean isValid(String transactionType, String amount, String date) {
        Date transactionDate = new Date(date);
        Date currentDate = new Date();
        return transactionDate.before(currentDate);
    }

}
