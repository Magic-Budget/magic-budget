//package me.magicbudget.controller;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import me.magicbudget.dto.TransactionCreateRequest;
//import me.magicbudget.model.Transaction;
//import me.magicbudget.model.User;
//import me.magicbudget.service.TransactionService;
//import me.magicbudget.service.UserService;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/transactions")
//public final class TransactionController {
//
//  private final TransactionService transactionService;
//  private final UserService userService;
//
//  public TransactionController(TransactionService transactionService, UserService userService) {
//    this.transactionService = transactionService;
//    this.userService = userService;
//  }
//
//  @PostMapping("/{id}")
//  public ResponseEntity<Transaction> createTransaction(@PathVariable UUID id,
//      @RequestBody TransactionCreateRequest newTransaction) {
//    try {
//      User user = userService.getUserById(id)
//          .orElse(null);
//
//      if (user == null) {
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//      }
//
////      Transaction transaction = new Transaction(null, user, newTransaction.getName(),
////          newTransaction.getTransactionDate(), newTransaction.getAmount(),
////          newTransaction.getDescription());
//
//      Transaction createdTransaction = transactionService.createTransaction(transaction);
//      return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
//    } catch (DataIntegrityViolationException e) {
//      return new ResponseEntity<>(HttpStatus.CONFLICT);
//    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }
//
//  @GetMapping("/{id}")
//  public ResponseEntity<Transaction> getTransactionById(@PathVariable UUID id) {
//    Optional<Transaction> transaction = transactionService.getTransactionById(id);
//    return transaction.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
//        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//  }
//
//  @GetMapping("/user/{userId}")
//  public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable UUID userId) {
//    List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
//    return new ResponseEntity<>(transactions, HttpStatus.OK);
//  }
//
//  @GetMapping("/dateRange")
//  public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
//      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
//      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
//    List<Transaction> transactions = transactionService.getTransactionsByTransactionDateBetween(
//        start, end);
//    return new ResponseEntity<>(transactions, HttpStatus.OK);
//  }
//
//  @PutMapping("/{id}")
//  public ResponseEntity<Transaction> updateTransaction(@PathVariable UUID id,
//      @RequestBody TransactionCreateRequest transactionRequest) {
//    Optional<Transaction> existingTransactionOpt = transactionService.getTransactionById(id);
//    if (existingTransactionOpt.isEmpty()) {
//      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    Transaction existingTransaction = existingTransactionOpt.get();
//
//    if (transactionRequest.getName() != null) {
//      existingTransaction.setName(transactionRequest.getName());
//    }
//    if (transactionRequest.getTransactionDate() != null) {
//      existingTransaction.setTransactionDate(transactionRequest.getTransactionDate());
//    }
//    if (transactionRequest.getAmount() != null) {
//      existingTransaction.setAmount(transactionRequest.getAmount());
//    }
//    if (transactionRequest.getDescription() != null) {
//      existingTransaction.setDescription(transactionRequest.getDescription());
//    }
//
//
//    Transaction updatedTransaction = transactionService.updateTransaction(existingTransaction);
//    return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
//  }
//
//  @DeleteMapping("/{id}")
//  public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
//    throw new UnsupportedOperationException("Not implemented");
//  }
//}