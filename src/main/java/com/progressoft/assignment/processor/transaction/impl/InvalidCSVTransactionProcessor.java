package com.progressoft.assignment.processor.transaction.impl;

import com.progressoft.assignment.processor.datastructure.Transaction;
import com.progressoft.assignment.processor.transaction.TransactionProcessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InvalidCSVTransactionProcessor implements TransactionProcessor {

    private List<Transaction> transactions = new LinkedList<>();

    public void process(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> transactions() {
        return new ArrayList<Transaction>(transactions);
    }

    @Override
    public void clearTransactions() {
        transactions.clear();
    }

}
