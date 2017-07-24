package com.progressoft.induction.tp.impl;

import com.progressoft.induction.tp.Transaction;
import com.progressoft.induction.tp.TransactionProcessor;
import com.progressoft.induction.tp.Violation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ahmad Y. Saleh on 7/24/17.
 */
public class CsvTransactionProcessor implements TransactionProcessor {

    @Override
    public List<Transaction> importTransactions(InputStream is) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            List<Transaction> transactionList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] transactionRecord = line.split(",");
                String amountString = transactionRecord[1];
                BigDecimal amount = getAmount(amountString);
                transactionList.add(new Transaction(transactionRecord[0], amount, transactionRecord[2]));
            }
            return transactionList;
        } catch (IOException e) {
            throw new IllegalStateException("Error while reading CSV from stream", e);
        }
    }

    private BigDecimal getAmount(String amountString) {
        try{
            return new BigDecimal(amountString);
        }catch (NumberFormatException e){
            return BigDecimal.ZERO;
        }
    }

    @Override
    public boolean isBalanced(List<Transaction> transactionList) {
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactionList) {
            balance = balance.add("C".equals(transaction.getType()) ? transaction.getAmount() : transaction.getAmount().negate());
        }
        return balance.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public List<Violation> validate(List<Transaction> transactionList) {
        List<Violation> violationList = new ArrayList<>();
        for (int i = 0; i < transactionList.size(); i++) {
            validateType(transactionList.get(i), violationList, i+1);
            validateAmount(transactionList.get(i), violationList, i+1);
        }
        return violationList;
    }

    private void validateType(Transaction transaction, List<Violation> violationList, int txOrder) {
        if (transaction.getType() == null || !Arrays.asList("C", "D").contains(transaction.getType())) {
            violationList.add(new Violation(txOrder, "type", "invalid type"));
        }
    }

    private void validateAmount(Transaction transaction, List<Violation> violationList, int txOrder) {
        if(transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) == 0){
            violationList.add(new Violation(txOrder, "amount", "invalid amount"));
        }
    }
}
