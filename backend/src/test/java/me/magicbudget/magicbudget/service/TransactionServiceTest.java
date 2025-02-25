package me.magicbudget.magicbudget.service;

import me.magicbudget.magicbudget.model.Transaction;
import me.magicbudget.magicbudget.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServiceTest {

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private UserService userService;

  @Test
  void testCreateTransaction() {
    // Create a test user
    User user = new User(null, "transaction_test_user1", "John", "Doe", "password123");
    User savedUser = userService.createUser(user);

    // Create a new transaction for the user
    LocalDateTime now = LocalDateTime.now();
    Transaction transaction = new Transaction(
        null,
        savedUser,
        "Grocery Shopping",
        now,
        new BigDecimal("100.00"),
        "Test transaction"
    );

    // Test the create transaction method
    Transaction savedTransaction = transactionService.createTransaction(transaction);

    // Assert the transaction was saved correctly
    assertNotNull(savedTransaction.id(), "Transaction ID should not be null");
    assertEquals(savedUser.id(), savedTransaction.user().id(), "User ID should match");
    assertEquals(now, savedTransaction.transactionDate(), "Transaction date should match");
    assertEquals(new BigDecimal("100.00"), savedTransaction.amount(), "Amount should match");
    assertEquals("Test transaction", savedTransaction.description(), "Description should match");
  }

  @Test
  void testGetTransactionById() {
    // Create a test user
    User user = new User(null, "transaction_test_user2", "Jane", "Smith", "password456");
    User savedUser = userService.createUser(user);

    // Create and save a transaction
    Transaction transaction = new Transaction(
        null,
        savedUser,
        "Paycheck Deposit",
        LocalDateTime.now(),
        new BigDecimal("50.00"),
        "Find by ID test"
    );
    Transaction savedTransaction = transactionService.createTransaction(transaction);

    // Test getting transaction by ID
    Optional<Transaction> retrievedTransactionOpt = transactionService.getTransactionById(
        savedTransaction.id());

    // Assert the transaction was retrieved correctly
    assertTrue(retrievedTransactionOpt.isPresent(), "Transaction should be present");
    Transaction retrievedTransaction = retrievedTransactionOpt.get();
    assertEquals(savedTransaction.id(), retrievedTransaction.id(), "Transaction ID should match");
    assertEquals(savedUser.id(), retrievedTransaction.user().id(), "User ID should match");
    assertEquals("Find by ID test", retrievedTransaction.description(), "Description should match");
  }

  @Test
  void testUpdateTransaction() {
    // Create a test user
    User user = new User(null, "transaction_test_user3", "Robert", "Johnson", "password789");
    User savedUser = userService.createUser(user);

    // Create and save a transaction
    Transaction transaction = new Transaction(
        null,
        savedUser,
        "Restaurant",
        LocalDateTime.now(),
        new BigDecimal("75.00"),
        "Original description"
    );
    Transaction savedTransaction = transactionService.createTransaction(transaction);

    // Update the transaction
    Transaction updatedTransactionData = new Transaction(
        savedTransaction.id(),
        savedTransaction.user(),
        savedTransaction.name(),
        savedTransaction.transactionDate(),
        savedTransaction.amount(),
        "Updated description"
    );

    // Test update transaction
    Transaction updatedTransaction = transactionService.updateTransaction(updatedTransactionData);

    // Assert the transaction was updated correctly
    assertEquals(savedTransaction.id(), updatedTransaction.id(), "ID should not change");
    assertEquals("Updated description", updatedTransaction.description(),
        "Description should be updated");
    assertEquals(savedTransaction.amount(), updatedTransaction.amount(),
        "Amount should remain the same");
  }

  @Test
  void testGetTransactionsByUserId() {
    // Create a test user
    User user = new User(null, "transaction_test_user4", "Emily", "Davis", "password101");
    User savedUser = userService.createUser(user);

    // Create and save multiple transactions for this user
    LocalDateTime now = LocalDateTime.now();

    Transaction transaction1 = new Transaction(
        null,
        savedUser,
        "Groceries",
        now.minusDays(1),
        new BigDecimal("30.00"),
        "Transaction 1"
    );

    Transaction transaction2 = new Transaction(
        null,
        savedUser,
        "Gas",
        now,
        new BigDecimal("45.00"),
        "Transaction 2"
    );

    transactionService.createTransaction(transaction1);
    transactionService.createTransaction(transaction2);

    // Test getting transactions by user ID
    List<Transaction> userTransactions = transactionService.getTransactionsByUserId(savedUser.id());

    // Assert the correct transactions were retrieved
    assertFalse(userTransactions.isEmpty(), "Transaction list should not be empty");
    assertEquals(2, userTransactions.size(), "Should have 2 transactions for this user");

    // Verify the transactions belong to the user
    for (Transaction tx : userTransactions) {
      assertEquals(savedUser.id(), tx.user().id(),
          "All transactions should belong to the test user");
    }
  }

  @Test
  void testGetTransactionsByTransactionDateBetween() {
    // Create a test user
    User user = new User(null, "transaction_test_user5", "Michael", "Brown", "password202");
    User savedUser = userService.createUser(user);

    // Create transactions with different dates
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime yesterday = now.minusDays(1);
    LocalDateTime twoDaysAgo = now.minusDays(2);
    LocalDateTime threeDaysAgo = now.minusDays(3);

    Transaction transaction1 = new Transaction(
        null,
        savedUser,
        "Coffee",
        yesterday,
        new BigDecimal("25.00"),
        "Yesterday"
    );

    Transaction transaction2 = new Transaction(
        null,
        savedUser,
        "Lunch",
        twoDaysAgo,
        new BigDecimal("35.00"),
        "Two days ago"
    );

    Transaction transaction3 = new Transaction(
        null,
        savedUser,
        "Paycheck",
        threeDaysAgo,
        new BigDecimal("45.00"),
        "Three days ago"
    );

    transactionService.createTransaction(transaction1);
    transactionService.createTransaction(transaction2);
    transactionService.createTransaction(transaction3);

    // Test getting transactions by date range (all three transactions)
    List<Transaction> dateRangeTransactions = transactionService.getTransactionsByTransactionDateBetween(
        threeDaysAgo, yesterday);

    // Assert the correct transactions were retrieved
    assertEquals(3, dateRangeTransactions.size(), "Should have 3 transactions in the date range");

    // Test getting transactions with a more specific date range
    List<Transaction> specificRangeTransactions = transactionService.getTransactionsByTransactionDateBetween(
        twoDaysAgo, yesterday.plusHours(1));

    assertEquals(2, specificRangeTransactions.size(),
        "Should have 2 transactions in the specific date range");
  }
}