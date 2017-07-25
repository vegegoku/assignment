package com.progressoft.induction.tp;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

/**
 * Created by Ahmad Y. Saleh on 7/24/17.
 */
public class XmlTransactionProcessorTest {

    private TransactionProcessor xmlTransactionProcessor;

    @Before
    public void setUp() {
        xmlTransactionProcessor = new TransactionProcessor(){

            @Override
            public void importTransactions(InputStream is) {
            }

            @Override
            public List<Transaction> getImportedTransactions() {
                return null;
            }

            @Override
            public List<Violation> validate() {
                return null;
            }

            @Override
            public boolean isBalanced() {
                return false;
            }
        };
    }

    @Test
    public void givenValidCsvStream_WhenImport_ThenReturnTheExpectedTransactions() {
        InputStream is = asStream("<TransactionList>\n" +
                "    <Transaction type=\"C\" amount=\"1000\" narration=\"salary\" />\n" +
                "    <Transaction type=\"D\" amount=\"200\" narration=\"rent\" />\n" +
                "    <Transaction type=\"D\" amount=\"800\" narration=\"other\" />\n" +
                "</TransactionList>");
        xmlTransactionProcessor.importTransactions(is);
        List<Transaction> transactions = xmlTransactionProcessor.getImportedTransactions();

        assertThat(transactions, containsInAnyOrder(
                newTransaction("D", new BigDecimal(200), "rent"),
                newTransaction("C", new BigDecimal(1000), "salary"),
                newTransaction("D", new BigDecimal(800), "other")
        ));
    }

    @Test
    public void givenBalancedCsvStream_WhenImportAndCheckIfBalanced_ThenReturnTrue() throws Exception {
        InputStream is = asStream("<TransactionList>\n" +
                "    <Transaction type=\"C\" amount=\"1000\" narration=\"salary\" />\n" +
                "    <Transaction type=\"D\" amount=\"200\" narration=\"rent\" />\n" +
                "    <Transaction type=\"D\" amount=\"800\" narration=\"other\" />\n" +
                "</TransactionList>");
        xmlTransactionProcessor.importTransactions(is);

        assertEquals(true, xmlTransactionProcessor.isBalanced());
    }

    @Test
    public void givenImbalancedCsvStream_WhenImportAndCheckIfBalanced_ThenReturnFalse() throws Exception {
        InputStream is = asStream("<TransactionList>\n" +
                "    <Transaction type=\"C\" amount=\"1000\" narration=\"salary\" />\n" +
                "    <Transaction type=\"D\" amount=\"400\" narration=\"rent\" />\n" +
                "    <Transaction type=\"D\" amount=\"750\" narration=\"other\" />\n" +
                "</TransactionList>");
        xmlTransactionProcessor.importTransactions(is);

        assertEquals(false, xmlTransactionProcessor.isBalanced());
    }

    @Test
    public void givenCsvStreamWithAnInvalidTransaction_WhenCallingValidate_ThenReportTheProperViolations() throws Exception {
        InputStream is = asStream("<TransactionList>\n" +
                "    <Transaction type=\"C\" amount=\"1000\" narration=\"salary\" />\n" +
                "    <Transaction type=\"X\" amount=\"400\" narration=\"rent\" />\n" +
                "    <Transaction type=\"D\" amount=\"750\" narration=\"other\" />\n" +
                "</TransactionList>");
        xmlTransactionProcessor.importTransactions(is);
        List<Violation> violations = xmlTransactionProcessor.validate();

        assertThat(violations, containsInAnyOrder(new Violation(2,"type")));
    }

    @Test
    public void givenCsvStreamWithMultipleInvalidTransactions_WhenCallingValidate_ThenReportTheProperViolations() throws Exception {
        InputStream is = asStream("<TransactionList>\n" +
                "    <Transaction type=\"C\" amount=\"one thousand\" narration=\"salary\" />\n" +
                "    <Transaction type=\"X\" amount=\"400\" narration=\"rent\" />\n" +
                "    <Transaction type=\"D\" amount=\"750\" narration=\"other\" />\n" +
                "</TransactionList>");
        xmlTransactionProcessor.importTransactions(is);
        List<Violation> violations = xmlTransactionProcessor.validate();

        assertThat(violations, containsInAnyOrder(new Violation(2,"type"),new Violation(1,"amount")));
    }

    @Test
    public void givenCsvStreamWithMultipleErrorsInSameTransaction_WhenCallingValidate_ThenReportTheProperViolations() throws Exception {
        InputStream is = asStream("<TransactionList>\n" +
                "    <Transaction type=\"C\" amount=\"one thousand\" narration=\"salary\" />\n" +
                "    <Transaction type=\"X\" amount=\"0\" narration=\"rent\" />\n" +
                "    <Transaction type=\"D\" amount=\"750\" narration=\"other\" />\n" +
                "</TransactionList>");
        xmlTransactionProcessor.importTransactions(is);
        List<Violation> violations = xmlTransactionProcessor.validate();

        assertThat(violations, containsInAnyOrder(new Violation(2,"type"),new Violation(2,"amount"),new Violation(1,"amount")));
    }

    private Transaction newTransaction(String type, BigDecimal amount, String narration) {
        return new Transaction(type, amount, narration);
    }

    private InputStream asStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }
}
