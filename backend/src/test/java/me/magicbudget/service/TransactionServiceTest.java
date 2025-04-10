/// / This code was generated with Claud 3.7 Thinker inside VScode with / the prompt "Create a test
/// class for the TransactionService class".
//package me.magicbudget.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import me.magicbudget.model.TransactionRequest;
//import me.magicbudget.model.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class TransactionServiceTest {
//
//  @Autowired
//  private TransactionService transactionService;
//
//  @Autowired
//  private UserService userService;
//
//  @Test
//  void testCreateTransaction() {
//    // Create a test user
//    User user = new User(null, "transaction_test_user1", "John", "Doe", "password123");
//    User savedUser = userService.createUser(user);
//
//    // Create a new transaction for the user
//    LocalDateTime now = LocalDateTime.now();
//    TransactionRequest transaction = new TransactionRequest(
//        null,
//        savedUser,
//        "Grocery Shopping",
//        now,
//        new BigDecimal("100.00"),
//        "Test transaction"
//    );
//
//    // Test the create transaction method
//    TransactionRequest savedTransaction = transactionService.createTransaction(transaction);
//
//    // Assert the transaction was saved correctly
//    assertNotNull(savedTransaction.getId(), "TransactionRequest ID should not be null");
//    assertEquals(savedUser.getId(), savedTransaction.getUser().getId(), "User ID should match");
//    assertEquals(now, savedTransaction.getTrasnactionDate(), "TransactionRequest date should match");
//    assertEquals(new BigDecimal("100.00"), savedTransaction.getAmount(), "Amount should match");
//    assertEquals("Test transaction", savedTransaction.getDescription(), "Description should match");
//  }
//
//  @Test
//  void testGetTransactionByGetId() {
//    // Create a test user
//    User user = new User(null, "transaction_test_user2", "Jane", "Smith", "password456");
//    User savedUser = userService.createUser(user);
//
//    // Create and save a transaction
//    TransactionRequest transaction = new TransactionRequest(
//        null,
//        savedUser,
//        "Paycheck Deposit",
//        LocalDateTime.now(),
//        new BigDecimal("50.00"),
//        "Find by ID test"
//    );
//    TransactionRequest savedTransaction = transactionService.createTransaction(transaction);
//
//    // Test getting transaction by ID
//    Optional<TransactionRequest> retrievedTransactionOpt = transactionService.getTransactionById(
//        savedTransaction.getId());
//
//    // Assert the transaction was retrieved correctly
//    assertTrue(retrievedTransactionOpt.isPresent(), "TransactionRequest should be present");
//    TransactionRequest retrievedTransaction = retrievedTransactionOpt.get();
//    assertEquals(savedTransaction.getId(), retrievedTransaction.getId(),
//        "TransactionRequest ID should match");
//    assertEquals(savedUser.getId(), retrievedTransaction.getUser().getId(), "User ID should match");
//    assertEquals("Find by ID test", retrievedTransaction.getDescription(),
//        "Description should match");
//  }
//
//  @Test
//  void testUpdateTransaction() {
//    // Create a test user
//    User user = new User(null, "transaction_test_user3", "Robert", "Johnson", "password789");
//    User savedUser = userService.createUser(user);
//
//    // Create and save a transaction
//    TransactionRequest transaction = new TransactionRequest(
//        null,
//        savedUser,
//        "Restaurant",
//        LocalDateTime.now(),
//        new BigDecimal("75.00"),
//        "Original description"
//    );
//    TransactionRequest savedTransaction = transactionService.createTransaction(transaction);
//
//    // Update the transaction
//    TransactionRequest updatedTransactionData = new TransactionRequest(
//        savedTransaction.getId(),
//        savedTransaction.getUser(),
//        savedTransaction.getName(),
//        savedTransaction.getTrasnactionDate(),
//        savedTransaction.getAmount(),
//        "Updated description"
//    );
//
//    // Test update transaction
//    TransactionRequest updatedTransaction = transactionService.updateTransaction(updatedTransactionData);
//
//    // Assert the transaction was updated correctly
//    assertEquals(savedTransaction.getId(), updatedTransaction.getId(), "ID should not change");
//    assertEquals("Updated description", updatedTransaction.getDescription(),
//        "Description should be updated");
//    assertEquals(savedTransaction.getAmount(), updatedTransaction.getAmount(),
//        "Amount should remain the same");
//  }
//
//  @Test
//  void testGetTransactionsByGetUserGetId() {
//    // Create a test user
//    User user = new User(null, "transaction_test_user4", "Emily", "Davis", "password101");
//    User savedUser = userService.createUser(user);
//
//    // Create and save multiple transactions for this user
//    LocalDateTime now = LocalDateTime.now();
//
//    TransactionRequest transaction1 = new TransactionRequest(
//        null,
//        savedUser,
//        "Groceries",
//        now.minusDays(1),
//        new BigDecimal("30.00"),
//        "TransactionRequest 1"
//    );
//
//    TransactionRequest transaction2 = new TransactionRequest(
//        null,
//        savedUser,
//        "Gas",
//        now,
//        new BigDecimal("45.00"),
//        "TransactionRequest 2"
//    );
//
//    transactionService.createTransaction(transaction1);
//    transactionService.createTransaction(transaction2);
//
//    // Test getting transactions by user ID
//    List<TransactionRequest> userTransactions = transactionService.getTransactionsByUserId(
//        savedUser.getId());
//
//    // Assert the correct transactions were retrieved
//    assertFalse(userTransactions.isEmpty(), "TransactionRequest list should not be empty");
//    assertEquals(2, userTransactions.size(), "Should have 2 transactions for this user");
//
//    // Verify the transactions belong to the user
//    for (TransactionRequest tx : userTransactions) {
//      assertEquals(savedUser.getId(), tx.getUser().getId(),
//          "All transactions should belong to the test user");
//    }
//  }
//
//  @Test
//  void testGetTransactionsByGetTrasnactionDateBetween() {
//    // Create a test user
//    User user = new User(null, "transaction_test_user5", "Michael", "Brown", "password202");
//    User savedUser = userService.createUser(user);
//
//    // Create transactions with different dates
//    LocalDateTime now = LocalDateTime.now();
//    LocalDateTime yesterday = now.minusDays(1);
//    LocalDateTime twoDaysAgo = now.minusDays(2);
//    LocalDateTime threeDaysAgo = now.minusDays(3);
//
//    TransactionRequest transaction1 = new TransactionRequest(
//        null,
//        savedUser,
//        "Coffee",
//        yesterday,
//        new BigDecimal("25.00"),
//        "Yesterday"
//    );
//
//    TransactionRequest transaction2 = new TransactionRequest(
//        null,
//        savedUser,
//        "Lunch",
//        twoDaysAgo,
//        new BigDecimal("35.00"),
//        "Two days ago"
//    );
//
//    TransactionRequest transaction3 = new TransactionRequest(
//        null,
//        savedUser,
//        "Paycheck",
//        threeDaysAgo,
//        new BigDecimal("45.00"),
//        "Three days ago"
//    );
//
//    transactionService.createTransaction(transaction1);
//    transactionService.createTransaction(transaction2);
//    transactionService.createTransaction(transaction3);
//
//    // Test getting transactions by date range (all three transactions)
//    List<TransactionRequest> dateRangeTransactions = transactionService.getTransactionsByTransactionDateBetween(
//        threeDaysAgo, yesterday);
//
//    // Assert the correct transactions were retrieved
//    assertEquals(3, dateRangeTransactions.size(), "Should have 3 transactions in the date range");
//
//    // Test getting transactions with a more specific date range
//    List<TransactionRequest> specificRangeTransactions = transactionService.getTransactionsByTransactionDateBetween(
//        twoDaysAgo, yesterday.plusHours(1));
//
//    assertEquals(2, specificRangeTransactions.size(),
//        "Should have 2 transactions in the specific date range");
//  }
//}