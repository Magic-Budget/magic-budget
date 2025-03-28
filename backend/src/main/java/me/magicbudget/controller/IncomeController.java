package me.magicbudget.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import me.magicbudget.dto.incoming_request.IncomeRequest;
import me.magicbudget.dto.outgoing_response.IncomeResponse;
import me.magicbudget.service.IncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/{userid}/income")
public class IncomeController {

  private final IncomeService incomeService;

  public IncomeController(IncomeService incomeService) {
    this.incomeService = incomeService;
  }

  @GetMapping("/view-all")
  public ResponseEntity<List<IncomeResponse>> findIncomeByUserId(
      @PathVariable("userid") UUID userId) {
    try {
      List<IncomeResponse> income = incomeService.viewAllIncome(userId);
      return new ResponseEntity<>(income, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("/add-income")
  public ResponseEntity<String> addIncome(@PathVariable("userid") UUID userId,
      @RequestBody IncomeRequest incomeRequest) {
    try {
      incomeService.addIncome(userId, incomeRequest);
      return new ResponseEntity<>("Income added successfully", HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      // Log the actual exception message to help debug
      System.err.println("Error adding income: " + e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
  }

  @GetMapping("/total")
  public ResponseEntity<BigDecimal> totalIncome(@PathVariable("userid") UUID userId) {
    try {
      BigDecimal bigDecimal = incomeService.totalIncome(userId);
      return new ResponseEntity<>(bigDecimal, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

}
