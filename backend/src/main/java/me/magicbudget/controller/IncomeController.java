package me.magicbudget.controller;

import me.magicbudget.dto.incomingrequest.IncomeRequest;
import me.magicbudget.dto.outgoingresponse.IncomeResponse;
import me.magicbudget.repository.IncomeRepository;
import me.magicbudget.repository.TransactionRepository;
import me.magicbudget.service.IncomeService;
import me.magicbudget.service.UserService;
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

@RestController
@RequestMapping("api/{userid}/income")
public class IncomeController {

  private final IncomeService incomeService;

  public IncomeController(IncomeService incomeService, IncomeRepository incomeRepository,
      TransactionRepository transactionRepository, UserService userService) {
    this.incomeService = incomeService;
  }

  @GetMapping("/view-all")
  public ResponseEntity<List<IncomeResponse>> findIncomeByUserId(@PathVariable("userid") String userId) {
    try {
      List<IncomeResponse> income = incomeService.viewAllIncome(userId);
      return new ResponseEntity<>(income, HttpStatus.OK);
    }
    catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("/add-income")
  public ResponseEntity<String> addIncome(@PathVariable("userid") String userId,
      @RequestBody IncomeRequest incomeRequest) {
    try{
      incomeService.addIncome(userId, incomeRequest);
      return new ResponseEntity<>("Income added successfully", HttpStatus.CREATED);
    }
    catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
  }

  @GetMapping("/total")
  public ResponseEntity<BigDecimal> totalIncome(@PathVariable("userid") String userId) {
    try {
      BigDecimal bigDecimal = incomeService.totalIncome(userId);
      return new ResponseEntity<>(bigDecimal, HttpStatus.OK);
    }
    catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
  }

}
