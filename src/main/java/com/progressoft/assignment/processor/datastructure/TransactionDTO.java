package com.progressoft.assignment.processor.datastructure;


public class TransactionDTO implements Transaction {

    private String transactionType;
    private String amount;
    private String date;

    public TransactionDTO(String transactionType, String amount, String date) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String getTransactionType() {
        return transactionType;
    }

    @Override
    public String getAmount() {
        return amount;
    }

    @Override
    public String getDate() {
        return date;
    }

}
