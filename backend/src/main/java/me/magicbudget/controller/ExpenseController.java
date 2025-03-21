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
import java.util.List;


@RestController
@RequestMapping("api/{userid}/expense")
public class ExpenseController {

  private final ExpenseService expenseService;

  public ExpenseController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }

  @GetMapping("/view-all")
  public ResponseEntity<List<ExpenseResponse>> findExpenseByUserId(@PathVariable("userid") String userId) {
    try {
      return new ResponseEntity<>(expenseService.viewExpenses(userId), HttpStatus.OK);
    }
    catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("/add-expense")
  public ResponseEntity<String> addExpense(@PathVariable("userid") String userId, @RequestBody
      ExpenseRequest expenseRequest) {
    try {
      expenseService.addExpense(userId, expenseRequest);
      return new ResponseEntity<>(HttpStatus.CREATED);
    }
    catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }


  }
}
