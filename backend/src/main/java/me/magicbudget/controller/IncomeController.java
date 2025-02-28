package me.magicbudget.controller;

import me.magicbudget.model.Income;
import me.magicbudget.model.User;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/income")
public class IncomeController {

  private final IncomeRepository incomeRepository;
  private final UserService userService;
  public IncomeController(IncomeRepository incomeRepository, UserService userService) {
    this.incomeRepository = incomeRepository;
    this.userService = userService;
  }

  @GetMapping("/view-all")
  public ResponseEntity<List<Income>> findIncomeByUserId(@RequestHeader("X-User-Id") UUID userId) {
    Optional<User> userById = userService.getUserById(userId);

    if (userById.isPresent()) {
      List<Income> incomes = incomeRepository.findIncomeByUserId(userById.get());
      return new ResponseEntity<>(incomes, HttpStatus.OK);
    }
    return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
  }

  @PostMapping("/add-income")
  public ResponseEntity<String> addIncome(@RequestHeader("X-User-Id") String userId, @RequestBody BigDecimal income) {
    Optional<User> userById = userService.getUserById(UUID.fromString(userId));
    if (userById.isPresent()) {
      User user = userById.get();
      Income income1 = new Income(user, income, LocalDateTime.now());
      incomeRepository.save(income1);
      return new ResponseEntity<>("Income added successfully", HttpStatus.CREATED);
    }
    return new ResponseEntity<>("User not found or not authorized", HttpStatus.FORBIDDEN);
  }

}
