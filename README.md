# Transactions Processor

### Summary
The _Transaction Processor_ is a system that allows users to import financial transactions (debits and credits of an account), it can handle transactions in different formats; namely CSV (comma-separated values) and XML.
Once the transactions are imported, the system can validate them and reports back any violation, it also can determine whether the imported transactions are balanced or not.

### Implementation Details
A transaction consists of the following:
* *type*: whether the transaction is debit or credit, the value will be _D_ for debit or _C_ for credit.
* *amount*: the amount of the transaction.
* *narration*: a description/purpose of the transaction

##### Supported Formats
The CSV format is `<type>,<amount>,<narration>`, see the example below:

```
D,61.22,Electricity bill
D,52.14,Social security payment
D,200.00,Payment sent to x
C,1920.00,Salary
D,150.00,Car rental
```

Following is a sample for the XML format:
```xml
<TransactionList>
    <Transaction type="D" amount="61.22" narration="Electricity bill" />
    <Transaction type="D" amount="52.14" narration="Social security payment" />
    <Transaction type="D" amount="200.00" narration="Payment sent to x" />
    <Transaction type="C" amount="1920.00" narration="Salary" />
    <Transaction type="D" amount="150.00" narration="Car rental" />
</TransactionList>
```

##### Implementation
You are provided with the interface `TransactionProcessor` that encapsulates the required operations. You are expected to write *two implementations* for it; one that handles the CSV format and the other for the XML.

The `TransactionProcessor` interface has four methods:
* `importTransactions`: this method accepts a `java.util.InputStream`, you should read this stream and parse its content into a `java.util.List` of `Transaction` (this class is provided for you)
* `getImportedTransactions`: this method returns the transactions list prepared by the `importTransactions` method
* `validate`: this method validates each imported transaction and returns the errors as a `List` of `Violation`s (this class is provided for you). Each `Violation` contains the name of the property that has the error (e.g. type, amount...), a description of the error and the order of the transaction that has this error.
* `isBalanced`: this method returns true if the amount sum of all the debit transactions equals the amount sum of all the credit transactions.


##### JUnit tests
JUnit is a framework to help you test your code and make sure that each peice of code works as intended.

You are provided with two JUnit test classes (for the CSV and XML implementations) that will help you understand what is required from you and help you test your work.

##### Maven
Maven is a software project management tool that helps you develop and build your project.

The Java project provided to you is a maven project, no prior knowledge of maven is reqquired. You can simply import the project to eclipse using the following steps:
* Save the project ZIP file and extract it
* Open Eclipse
* Go to _File_ --> _Import..._
* Select _Maven_ --> _Existing Maven Projects_ and click _Next_
* _Browse_ to the extracted project directory and click _OK_
* Click _Finish_

##### Delivery
You should create and sent two implementations of the `TransactionProcessor` interface, one that handles XML and another for CSV. You must not modify the `TransactionProcessor` interface itself.
