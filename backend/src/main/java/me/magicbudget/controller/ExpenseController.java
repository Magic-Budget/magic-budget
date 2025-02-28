package me.magicbudget.controller;

import me.magicbudget.dto.ExpenseRequest;
import me.magicbudget.model.Expense;
import me.magicbudget.model.Income;
import me.magicbudget.model.User;
import me.magicbudget.repository.ExpenseRepository;
import me.magicbudget.repository.IncomeRepository;
import me.magicbudget.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/expense")
public class ExpenseController {

  private final ExpenseRepository expenseRepository;
  private final UserService userService;

  public ExpenseController(ExpenseRepository expenseRepository, UserService userService) {
    this.expenseRepository = expenseRepository;
    this.userService = userService;
  }

  @GetMapping("/view-all")
  public ResponseEntity<List<Expense>> findExpenseByUserId(@RequestHeader("X-User-Id") UUID userId) {
    Optional<User> userById = userService.getUserById(userId);

    if (userById.isPresent()) {
      List<Expense> expenses = expenseRepository.findExpenseByUserId(userById.get());
      return new ResponseEntity<>(expenses, HttpStatus.OK);
    }
    return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
  }

  @PostMapping("/add-expense")
  public ResponseEntity<String> addExpense(@RequestHeader("X-User-Id") String userId, @RequestBody
      ExpenseRequest expenseRequest) {
    Optional<User> userById = userService.getUserById(UUID.fromString(userId));
    if (userById.isPresent()) {
      User user = userById.get();
      Expense expense = new Expense(user,expenseRequest.getAmount(),expenseRequest.getExpenseDate());
      expenseRepository.save(expense);
      return new ResponseEntity<>(HttpStatus.CREATED);
    }
    return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

  }


}
