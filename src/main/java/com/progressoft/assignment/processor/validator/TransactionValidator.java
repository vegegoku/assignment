package com.progressoft.assignment.processor.validator;

import com.progressoft.assignment.processor.datastructure.Transaction;

public interface TransactionValidator {
    boolean isValid(String transactionType,String amount,String date);
}
