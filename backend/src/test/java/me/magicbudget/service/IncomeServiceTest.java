// This code was generated with Claud 3.5 Sonnet inside IntelliJ IDEA with
// the prompt "Create a test class for the IncomeService class".

package me.magicbudget.service;

import me.magicbudget.dto.incoming_request.IncomeRequest;
import me.magicbudget.dto.incoming_request.RegistrationAndAuthRequest;
import me.magicbudget.dto.outgoing_response.IncomeResponse;
import me.magicbudget.model.*;
import me.magicbudget.repository.IncomeRepository;
import me.magicbudget.repository.TransactionRepository;
import me.magicbudget.security.service.RegistrationAndAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IncomeServiceTest {

  @Autowired
  private IncomeService incomeService;

  @Autowired
  private UserService userService;

  @Autowired
  private IncomeRepository incomeRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private RegistrationAndAuthService registrationService;

  private User testUser;

  @BeforeEach
  void setup() {
    var userInfo = new UserInformation("goaluser",
        "password123",
        "John",
        "Doe",
        "");

    RegistrationAndAuthRequest request = new RegistrationAndAuthRequest(
        userInfo.getUsername(),
        userInfo.getPassword(),
        userInfo.getFirstName(),
        userInfo.getLastName(),
        userInfo.getEmail()
    );
    registrationService.registerUser(request);

    var responseInfo = userService.getUserByUsername(userInfo.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

    testUser = new User(responseInfo);
  }

  @Test
  void testAddIncome() {
    var request = new IncomeRequest(
        "Salary",
        "Monthly salary",
        BigDecimal.valueOf(1000),
        IncomeType.MONTHLY,
        LocalDateTime.now()
    );

    assertDoesNotThrow(() -> incomeService.addIncome(testUser.getId(), request));

    var incomes = incomeRepository.findIncomeByUserId(testUser);
    assertEquals(1, incomes.size());
    assertEquals("Salary", incomes.get(0).getTransaction().getName());
  }

  @Test
  void testViewAllIncome() {
    // Add test income
    var request = new IncomeRequest(
        "Bonus",
        "Year-end bonus",
        BigDecimal.valueOf(500),
        IncomeType.ONETIME,
        LocalDateTime.now()
    );
    incomeService.addIncome(testUser.getId(), request);

    var responses = incomeService.viewAllIncome(testUser.getId());
    assertEquals(1, responses.size());
    IncomeResponse incomeResponse = responses.get(0);
    assertEquals("Bonus", incomeResponse.income_name());
    assertEquals(BigDecimal.valueOf(500), incomeResponse.income_amount());
  }

  @Test
  void testTotalIncome() {
    // Add monthly income from last month
    var monthlyRequest = new IncomeRequest(
        "Salary",
        "Monthly Salary",
        BigDecimal.valueOf(1000),
        IncomeType.MONTHLY,
        LocalDateTime.now().minusMonths(1)
    );
    incomeService.addIncome(testUser.getId(), monthlyRequest);

    var request = new IncomeRequest(
        "Bonus",
        "Year-end bonus",
        BigDecimal.valueOf(500),
        IncomeType.ONETIME,
        LocalDateTime.now()
    );
    incomeService.addIncome(testUser.getId(), request);

    var total = incomeService.totalIncome(testUser.getId());
    assertTrue(total.compareTo(BigDecimal.ZERO) > 0);
    assertEquals(total, BigDecimal.valueOf(1500));
  }

  @Test
  void testAddIncomeWithInvalidUser() {
    var request = new IncomeRequest(
        "Bonus",
        "Year-end bonus",
        BigDecimal.valueOf(500),
        IncomeType.ONETIME,
        LocalDateTime.now()
    );

    assertThrows(IllegalArgumentException.class,
        () -> incomeService.addIncome(UUID.randomUUID(), request));
  }
}