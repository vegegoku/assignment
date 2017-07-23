package com.progressoft.assignment.processor.file;

import com.progressoft.assignment.processor.datastructure.TransactionDTO;
import com.progressoft.assignment.processor.file.exception.InvalidFilePathException;
import com.progressoft.assignment.processor.transaction.TransactionProcessor;
import com.progressoft.assignment.processor.transaction.exception.NullProcessorException;
import com.progressoft.assignment.processor.validator.TransactionValidator;
import com.progressoft.assignment.processor.validator.exception.NullValidatorException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.isNull;

public abstract class AbstractProcessor implements Processor {

    protected List<TransactionValidator> validators = new LinkedList<>();
    protected TransactionProcessor validTransactionProcessor;
    protected TransactionProcessor invalidTransactionProcessor;

    private int invalidTransactions;
    private double fileBalance;

    @Override
    public void attachTransactionValidator(TransactionValidator validator) {
        if(isNull(validator))
            throw new NullValidatorException();
        validators.add(validator);
    }

    @Override
    public void process(File file) {
        if(isNull(file) || !file.exists() || file.isDirectory())
            throw new InvalidFilePathException();
        if(isNull(validTransactionProcessor) || isNull(invalidTransactionProcessor))
            throw new NullProcessorException();
        invalidTransactions = 0;
        fileBalance = 0;
        validTransactionProcessor.clearTransactions();
        invalidTransactionProcessor.clearTransactions();
        startProcess(file);
    }

    protected abstract void startProcess(File file);

    @Override
    public void configureValidTransactionProcessor(TransactionProcessor validTransactionProcessor) {
        if(isNull(validTransactionProcessor))
            throw new NullProcessorException();
        this.validTransactionProcessor = validTransactionProcessor;
    }

    @Override
    public void configureInvalidTransactionProcessor(TransactionProcessor invalidTransactionProcessor) {
        if(isNull(invalidTransactionProcessor))
            throw new NullProcessorException();
        this.invalidTransactionProcessor = invalidTransactionProcessor;
    }


    protected void processValidTransaction(String transactionType,String amount,String date) {
        if(transactionType.toLowerCase().equals("debit"))
            fileBalance += Double.parseDouble(amount);
        if(transactionType.toLowerCase().equals("cridit"))
            fileBalance -= Double.parseDouble(amount);
        validTransactionProcessor.process(new TransactionDTO(transactionType,amount,date));
    }

    protected void processInvalidTransaction(String transactionType,String amount,String date) {
        ++invalidTransactions;
        invalidTransactionProcessor.process(new TransactionDTO(transactionType,amount,date));
    }

    @Override
    public boolean isBalanced() {
        return invalidTransactions == 0 && (Math.abs(fileBalance - 0) < 1e-6);
    }

}
