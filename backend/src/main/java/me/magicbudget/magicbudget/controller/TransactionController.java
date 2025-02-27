package me.magicbudget.magicbudget.controller;

import me.magicbudget.magicbudget.model.CategoryTotals;
import me.magicbudget.magicbudget.model.Transaction;
import me.magicbudget.magicbudget.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
    Transaction createdTransaction = transactionService.createTransaction(transaction);
    return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
  }

  @GetMapping("/transactions/id/{id}")
  public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
    Optional<Transaction> transactionOptional = transactionService.getTransactionById(id);
    return transactionOptional.map(value->new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/transactions/month/{month}/{year}")
  public ResponseEntity<List<Transaction>>  getTransactionByMonth(@PathVariable int month, @PathVariable int year) {
    if (month < 1 || month > 12) { return ResponseEntity.badRequest().build();}

    LocalDate initial;
    LocalDate finalDay;

    try {
      initial = LocalDate.of(year, month, 1);
      finalDay = initial.plusMonths(1).minusDays(1);
    }
    catch (Exception e){
      return ResponseEntity.badRequest().build();
    }

    List<Transaction> transactions = transactionService.getTransactionsByTransactionDateBetween(initial, finalDay);
    return transactions.isEmpty()
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(transactions);
  }

  @GetMapping("/transactions/category-totals")
  public ResponseEntity<List<CategoryTotals>> getTransactionByCategoryTotals() {
    List<CategoryTotals> categoryTotals = transactionService.getCategoryTotals();
    return categoryTotals.isEmpty()
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(categoryTotals);
  }

  @PutMapping
  public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction) {
    Transaction createdTransaction = transactionService.updateTransaction(transaction);
    return new ResponseEntity<>(createdTransaction, HttpStatus.ACCEPTED);
  }
}