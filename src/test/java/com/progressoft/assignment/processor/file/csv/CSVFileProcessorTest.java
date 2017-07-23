package com.progressoft.assignment.processor.file.csv;

import com.progressoft.assignment.processor.file.Processor;
import com.progressoft.assignment.processor.file.exception.InvalidFilePathException;
import com.progressoft.assignment.processor.transaction.TransactionProcessor;
import com.progressoft.assignment.processor.transaction.exception.NullProcessorException;
import com.progressoft.assignment.processor.transaction.impl.InvalidCSVTransactionProcessor;
import com.progressoft.assignment.processor.transaction.impl.ValidCSVTransactionProcessor;
import com.progressoft.assignment.processor.validator.TransactionValidator;
import com.progressoft.assignment.processor.validator.impl.AmountValidator;
import com.progressoft.assignment.processor.validator.impl.DateValidator;
import com.progressoft.assignment.processor.validator.exception.NullValidatorException;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVFileProcessorTest {

    private static final String RESOURCES_ROOT = "./src/test/resources/";
    private static final String NON_EXSITING_FILE_PATH = "NON_EXSITING_FILE_PATH";
    private static final String CSV_DIRECTORY = "csv/";
    private static final String IMBALANCED_TRANSACTIONS_FILE = "imbalanced.txt";
    private static final String BALANCED_TRANSACTIONS_FILE = "balanced.txt";
    private static final String PARTIALLY_VALID_FILE = "partiallyValid.txt";
    private static final String INVALID_FILE = "invalid.txt";

    private Processor processor;
    private TransactionProcessor validTransactionProcessor;
    private TransactionProcessor invalidTransactionProcessor;
    private TransactionValidator amountValidator;
    private TransactionValidator dateValidator;

    @Before
    public void setUp() {
        processor = new CSVFileProcessor();
        validTransactionProcessor = new ValidCSVTransactionProcessor();
        invalidTransactionProcessor = new InvalidCSVTransactionProcessor();
        amountValidator = new AmountValidator();
        dateValidator = new DateValidator();
    }

    @Test(expected = InvalidFilePathException.class)
    public void givenCSVFileProcessor_WhenCallingProcessAndPassingFileWithNonExistingFilePath_ThenShouldThrowInvalidFilePath() {
        processor.process(Paths.get(NON_EXSITING_FILE_PATH).toFile());
    }

    @Test(expected = InvalidFilePathException.class)
    public void givenCSVFileProcessor_WhenCallingProcessAndPassingFileWithDirectoryPath_ThenShouldThrowInvalidFilePath() {
        processor.process(Paths.get(RESOURCES_ROOT).toFile());
    }

    @Test(expected = InvalidFilePathException.class)
    public void givenCSVFileProcessor_WhenCallingProcessAndPassingNullFile_ThenShouldThrowInvalidFilePath() {
        processor.process(null);
    }

    @Test(expected = NullProcessorException.class)
    public void givenCSVFileProcessor_WhenCallingConfigureValidTransactionProcessorAndPassingNullProcessor_ThenShouldThrowNullValidTransactionProcessor() {
        processor.configureValidTransactionProcessor(null);
    }

    @Test(expected = NullProcessorException.class)
    public void givenCSVFileProcessor_WhenCallingConfigureInvalidTransactionProcessorAndPassingNullProcessor_ThenShouldThrowNullInvalidTransactionProcessor() {
        processor.configureInvalidTransactionProcessor(null);
    }

    @Test(expected = NullValidatorException.class)
    public void givenCSVFileProcessor_WhenCallingAttachTransactionValidatorAndPassingNullValidator_ThenShouldThrowNullTransactionValidator() {
        processor.attachTransactionValidator(null);
    }

    @Test(expected = NullProcessorException.class)
    public void givenCSVFileProcessor_WhenCallingProcess_ThenNullValidTransactionProcessorShouldBeThrown() {
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + IMBALANCED_TRANSACTIONS_FILE).toFile());
    }

    @Test(expected = NullProcessorException.class)
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidTransactionProcessor_ThenNullInvalidTransactionProcessorShouldBeThrown() {
        processor.configureValidTransactionProcessor(validTransactionProcessor);
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + IMBALANCED_TRANSACTIONS_FILE).toFile());
    }

    private void initProcessor() {
        processor.configureValidTransactionProcessor(validTransactionProcessor);
        processor.configureInvalidTransactionProcessor(invalidTransactionProcessor);
        processor.attachTransactionValidator(amountValidator);
        processor.attachTransactionValidator(dateValidator);
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessors_ThenShouldProcessPartiallyValidTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + PARTIALLY_VALID_FILE).toFile());
        assertEquals(1,validTransactionProcessor.transactions().size());
        assertEquals(1,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessorsTwice_ThenShouldProcessPartiallyValidTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + PARTIALLY_VALID_FILE).toFile());
        assertEquals(1,validTransactionProcessor.transactions().size());
        assertEquals(1,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + PARTIALLY_VALID_FILE).toFile());
        assertEquals(1,validTransactionProcessor.transactions().size());
        assertEquals(1,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessors_ThenShouldProcessBalancedTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + BALANCED_TRANSACTIONS_FILE).toFile());
        assertEquals(5,validTransactionProcessor.transactions().size());
        assertEquals(0,invalidTransactionProcessor.transactions().size());
        assertTrue(processor.isBalanced());
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessors_ThenShouldProcessLargeBalancedTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + "balanced-1000000.txt").toFile());
        assertEquals(1000000,validTransactionProcessor.transactions().size());
        assertEquals(0,invalidTransactionProcessor.transactions().size());
        assertTrue(processor.isBalanced());
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessorsTwice_ThenShouldProcessBalancedTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + BALANCED_TRANSACTIONS_FILE).toFile());
        assertEquals(5,validTransactionProcessor.transactions().size());
        assertEquals(0,invalidTransactionProcessor.transactions().size());
        assertTrue(processor.isBalanced());
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + BALANCED_TRANSACTIONS_FILE).toFile());
        assertEquals(5,validTransactionProcessor.transactions().size());
        assertEquals(0,invalidTransactionProcessor.transactions().size());
        assertTrue(processor.isBalanced());
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessors_ThenShouldProcessImbalancedTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + IMBALANCED_TRANSACTIONS_FILE).toFile());
        assertEquals(2,validTransactionProcessor.transactions().size());
        assertEquals(0,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessorsTwice_ThenShouldProcessImbalancedTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + IMBALANCED_TRANSACTIONS_FILE).toFile());
        assertEquals(2,validTransactionProcessor.transactions().size());
        assertEquals(0,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + IMBALANCED_TRANSACTIONS_FILE).toFile());
        assertEquals(2,validTransactionProcessor.transactions().size());
        assertEquals(0,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessors_ThenShouldProcessInvalidTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + INVALID_FILE).toFile());
        assertEquals(0,validTransactionProcessor.transactions().size());
        assertEquals(6,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
    }

    @Test
    public void givenCSVFileProcessor_WhenCallingStartProcessAndConfigureValidandInvalidTransactionProcessorsTwice_ThenShouldProcessInvalidTransactionsFileTheRecords() {
        initProcessor();
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + INVALID_FILE).toFile());
        assertEquals(0,validTransactionProcessor.transactions().size());
        assertEquals(6,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
        processor.process(Paths.get(RESOURCES_ROOT + CSV_DIRECTORY + INVALID_FILE).toFile());
        assertEquals(0,validTransactionProcessor.transactions().size());
        assertEquals(6,invalidTransactionProcessor.transactions().size());
        assertFalse(processor.isBalanced());
    }



}
