package com.progressoft.assignment.processor.file.csv;

import com.progressoft.assignment.processor.file.AbstractProcessor;
import com.progressoft.assignment.processor.validator.TransactionValidator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CSVFileProcessor extends AbstractProcessor {

    private static final int TRANSACTION_TYPE = 0;
    private static final int AMOUNT = 1;
    private static final int DATE = 2;

    @Override
    protected void startProcess(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            readCSVFile(fileReader);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void readCSVFile(FileReader fileReader) throws IOException {
        try (BufferedReader reader = new BufferedReader(fileReader)) {
            String transaction = null;
            while ((transaction = reader.readLine()) != null) {
                String [] splittedTransaction = transaction.split(",");
                if(isValidTransaction(splittedTransaction))
                    processValidTransaction(splittedTransaction[TRANSACTION_TYPE],splittedTransaction[AMOUNT],splittedTransaction[DATE]);
                else
                    processInvalidTransaction(splittedTransaction[TRANSACTION_TYPE],splittedTransaction[AMOUNT],splittedTransaction[DATE]);
            }
        }
    }

    private boolean isValidTransaction(String[] transaction) {
        boolean isValid = true;
        for (TransactionValidator validator : validators)
            isValid &= validator.isValid(transaction[TRANSACTION_TYPE],transaction[AMOUNT],transaction[DATE]);
        return isValid;
    }

}
