package com.progressoft.assignment.processor.file;

import com.progressoft.assignment.processor.transaction.TransactionProcessor;
import com.progressoft.assignment.processor.validator.TransactionValidator;

import java.io.File;

public interface Processor {

    void process(File file);

    void attachTransactionValidator(TransactionValidator validator);

    void configureValidTransactionProcessor(TransactionProcessor validTransactionProcessor);

    void configureInvalidTransactionProcessor(TransactionProcessor invalidTransactionProcessor);

    boolean isBalanced();

}
