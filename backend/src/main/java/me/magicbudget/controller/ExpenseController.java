package me.magicbudget.controller;

import me.magicbudget.dto.incoming_request.ExpenseRequest;
import me.magicbudget.dto.outgoing_response.ExpenseResponse;
import me.magicbudget.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/{userid}/expense")
public class ExpenseController {

  private final ExpenseService expenseService;

  public ExpenseController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @GetMapping("/view-all")
  public ResponseEntity<List<ExpenseResponse>> findExpenseByUserId(
      @PathVariable("userid") UUID userId) {
    try {
      return new ResponseEntity<>(expenseService.viewExpenses(userId), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/view/{expenseid}")
  public ResponseEntity<ExpenseResponse> findExpenseById(@PathVariable("userid") UUID userId,
      @PathVariable("expenseid") UUID expenseId) {
    try {
      return new ResponseEntity<>(expenseService.viewExpense(userId, expenseId), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("/add-expense")
  public ResponseEntity<String> addExpense(@PathVariable("userid") UUID userId, @RequestBody
  ExpenseRequest expenseRequest) {
    try {
      expenseService.addExpense(userId, expenseRequest);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

  @GetMapping("/total")
  public ResponseEntity<BigDecimal> totalExpense(@PathVariable("userid") UUID userId) {
    try {
      BigDecimal totalExpense = expenseService.getTotalExpense(userId);
      return new ResponseEntity<>(totalExpense, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

  }

  @PostMapping("/split/")
  public ResponseEntity<Boolean> splitExpense(@PathVariable("userid") UUID userID,
      @RequestBody List<UUID> split_with, @RequestBody UUID expenseID) {
    try {
      return new ResponseEntity<>(expenseService.splitExpense(userID, expenseID, split_with),
          HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
