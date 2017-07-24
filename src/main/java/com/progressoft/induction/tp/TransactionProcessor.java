package com.progressoft.induction.tp;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Ahmad Y. Saleh on 7/24/17.
 */
public interface TransactionProcessor {

    void importTransactions(InputStream is);

    List<Transaction> getImportedTransactions();

    List<Violation> validate();

    boolean isBalanced();
}
