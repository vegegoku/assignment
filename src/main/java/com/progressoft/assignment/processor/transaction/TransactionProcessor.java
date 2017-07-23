package com.progressoft.assignment.processor.transaction;


import com.progressoft.assignment.processor.datastructure.Transaction;

import java.util.List;

public interface TransactionProcessor {
    void process(Transaction transaction);
    List<Transaction> transactions();
    void clearTransactions();
}
