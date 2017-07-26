package com.progressoft.induction.tp;

import java.math.BigDecimal;

/**
 * Created by Ahmad Y. Saleh on 7/24/17.
 */
public class Transaction {

    private String type;
    private BigDecimal amount;
    private String narration;

    public Transaction(String type, BigDecimal amount, String narration) {
        this.type = type;
        this.amount = amount;
        this.narration = narration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        return narration != null ? narration.equals(that.narration) : that.narration == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (narration != null ? narration.hashCode() : 0);
        return result;
    }

}
