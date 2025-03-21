// This code was generated with Claud 3.7 Thinker inside VScode with
// the prompt "Create a test class for the CreditDebtService class".
/*package me.magicbudget.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.Business;
import me.magicbudget.model.CreditDebt;
import me.magicbudget.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CreditDebtServiceTest {

  @Autowired
  private CreditDebtService creditDebtService;

  @Autowired
  private UserService userService;

  @Autowired
  private BusinessService businessService;

  @Test
  void testCreateCreditDebt() {
    // Create a test user
    User user = new User(null, "creditdebt_user1", "John", "Doe", "password123");
    User savedUser = userService.createUser(user);

    // Create a test business
    Business business = new Business();
    business.setName("Chase Bank");
    Business savedBusiness = businessService.createBusiness(business);

    // Create a credit debt
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setBusiness(savedBusiness);
    creditDebt.setAmount(new BigDecimal("5000.00"));
    creditDebt.setInterestRate(new BigDecimal("0.0549"));  // 5.49%

    // Save the credit debt
    CreditDebt savedCreditDebt = creditDebtService.createCreditDebt(creditDebt, savedUser.getId());

    // Assertions
    assertNotNull(savedCreditDebt.getId(), "Credit debt ID should not be null");
    assertEquals(savedUser.getId(), savedCreditDebt.getUser().getId(), "User ID should match");
    assertEquals(savedBusiness.getId(), savedCreditDebt.getBusiness().getId(),
        "Business ID should match");
    assertEquals(new BigDecimal("5000.00"), savedCreditDebt.getAmount(), "Amount should match");
    assertEquals(new BigDecimal("0.0549"), savedCreditDebt.getInterestRate(),
        "Interest rate should match");
  }

  @Test
  void testGetCreditDebtById() {
    // Create a test user
    User user = new User(null, "creditdebt_user2", "Jane", "Smith", "password456");
    User savedUser = userService.createUser(user);

    // Create a test business
    Business business = new Business();
    business.setName("Bank of America");
    Business savedBusiness = businessService.createBusiness(business);

    // Create and save a credit debt
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setBusiness(savedBusiness);
    creditDebt.setAmount(new BigDecimal("2500.50"));
    creditDebt.setInterestRate(new BigDecimal("0.0649"));  // 6.49%
    CreditDebt savedCreditDebt = creditDebtService.createCreditDebt(creditDebt, savedUser.getId());

    // Retrieve the credit debt
    Optional<CreditDebt> retrievedCreditDebtOpt = creditDebtService.getCreditDebtById(
        savedCreditDebt.getId());

    // Assertions
    assertTrue(retrievedCreditDebtOpt.isPresent(), "Credit debt should be present");
    CreditDebt retrievedCreditDebt = retrievedCreditDebtOpt.get();
    assertEquals(savedCreditDebt.getId(), retrievedCreditDebt.getId(),
        "Credit debt ID should match");
    assertEquals(savedUser.getId(), retrievedCreditDebt.getUser().getId(), "User ID should match");
    assertEquals(savedBusiness.getId(), retrievedCreditDebt.getBusiness().getId(),
        "Business ID should match");
  }

  @Test
  void testGetCreditDebtsByUserId() {
    // Create a test user
    User user = new User(null, "creditdebt_user3", "Bob", "Johnson", "password789");
    User savedUser = userService.createUser(user);

    // Create test businesses
    Business business1 = new Business();
    business1.setName("Discover");
    Business savedBusiness1 = businessService.createBusiness(business1);

    Business business2 = new Business();
    business2.setName("Citi");
    Business savedBusiness2 = businessService.createBusiness(business2);

    // Create and save multiple credit debts
    CreditDebt creditDebt1 = new CreditDebt();
    creditDebt1.setBusiness(savedBusiness1);
    creditDebt1.setAmount(new BigDecimal("3000.00"));
    creditDebt1.setInterestRate(new BigDecimal("0.0749"));  // 7.49%
    creditDebtService.createCreditDebt(creditDebt1, savedUser.getId());

    CreditDebt creditDebt2 = new CreditDebt();
    creditDebt2.setBusiness(savedBusiness2);
    creditDebt2.setAmount(new BigDecimal("7500.00"));
    creditDebt2.setInterestRate(new BigDecimal("0.0849"));  // 8.49%
    creditDebtService.createCreditDebt(creditDebt2, savedUser.getId());

    // Retrieve credit debts for the user
    List<CreditDebt> userCreditDebts = creditDebtService.getCreditDebtsByUserId(savedUser.getId());

    // Assertions
    assertEquals(2, userCreditDebts.size(), "User should have 2 credit debts");
    assertTrue(
        userCreditDebts.stream().anyMatch(debt -> debt.getBusiness().getName().equals("Discover")),
        "Should contain Discover credit debt");
    assertTrue(
        userCreditDebts.stream().anyMatch(debt -> debt.getBusiness().getName().equals("Citi")),
        "Should contain Citi credit debt");
  }

  @Test
  void testUpdateCreditDebt() {
    // Create a test user
    User user = new User(null, "creditdebt_user4", "Alice", "Brown", "password101");
    User savedUser = userService.createUser(user);

    // Create a test business
    Business business = new Business();
    business.setName("Capital One");
    Business savedBusiness = businessService.createBusiness(business);

    // Create and save a credit debt
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setBusiness(savedBusiness);
    creditDebt.setAmount(new BigDecimal("10000.00"));
    creditDebt.setInterestRate(new BigDecimal("0.0699"));  // 6.99%
    CreditDebt savedCreditDebt = creditDebtService.createCreditDebt(creditDebt, savedUser.getId());

    // Update the credit debt
    savedCreditDebt.setAmount(new BigDecimal("8500.00"));
    savedCreditDebt.setInterestRate(new BigDecimal("0.0599"));  // 5.99%
    CreditDebt updatedCreditDebt = creditDebtService.updateCreditDebt(savedCreditDebt);

    // Assertions
    assertEquals(new BigDecimal("8500.00"), updatedCreditDebt.getAmount(),
        "Updated amount should match");
    assertEquals(new BigDecimal("0.0599"), updatedCreditDebt.getInterestRate(),
        "Updated interest rate should match");
  }

  @Test
  void testDeleteCreditDebt() {
    // Create a test user
    User user = new User(null, "creditdebt_user5", "Tom", "Wilson", "password202");
    User savedUser = userService.createUser(user);

    // Create a test business
    Business business = new Business();
    business.setName("Wells Fargo");
    Business savedBusiness = businessService.createBusiness(business);

    // Create and save a credit debt
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setBusiness(savedBusiness);
    creditDebt.setAmount(new BigDecimal("4000.00"));
    creditDebt.setInterestRate(new BigDecimal("0.0799"));  // 7.99%
    CreditDebt savedCreditDebt = creditDebtService.createCreditDebt(creditDebt, savedUser.getId());

    // Delete the credit debt
    creditDebtService.deleteCreditDebt(savedCreditDebt.getId());

    // Try to retrieve the deleted credit debt
    Optional<CreditDebt> deletedCreditDebt = creditDebtService.getCreditDebtById(
        savedCreditDebt.getId());

    // Assertions
    assertFalse(deletedCreditDebt.isPresent(), "Credit debt should be deleted");
  }

  @Test
  void testDeleteAllCreditDebtsByUserId() {
    // Create a test user
    User user = new User(null, "creditdebt_user6", "Emily", "Davis", "password303");
    User savedUser = userService.createUser(user);

    // Create test businesses
    Business business1 = new Business();
    business1.setName("American Express");
    Business savedBusiness1 = businessService.createBusiness(business1);

    Business business2 = new Business();
    business2.setName("USAA");
    Business savedBusiness2 = businessService.createBusiness(business2);

    // Create and save multiple credit debts
    CreditDebt creditDebt1 = new CreditDebt();
    creditDebt1.setBusiness(savedBusiness1);
    creditDebt1.setAmount(new BigDecimal("12000.00"));
    creditDebt1.setInterestRate(new BigDecimal("0.0999"));  // 9.99%
    creditDebtService.createCreditDebt(creditDebt1, savedUser.getId());

    CreditDebt creditDebt2 = new CreditDebt();
    creditDebt2.setBusiness(savedBusiness2);
    creditDebt2.setAmount(new BigDecimal("6000.00"));
    creditDebt2.setInterestRate(new BigDecimal("0.0399"));  // 3.99%
    creditDebtService.createCreditDebt(creditDebt2, savedUser.getId());

    // Verify credit debts exist
    List<CreditDebt> userCreditDebtsBefore = creditDebtService.getCreditDebtsByUserId(
        savedUser.getId());
    assertEquals(2, userCreditDebtsBefore.size(), "User should have 2 credit debts initially");

    // Delete all credit debts for the user
    creditDebtService.deleteAllCreditDebtsByUserId(savedUser.getId());

    // Verify credit debts are deleted
    List<CreditDebt> userCreditDebtsAfter = creditDebtService.getCreditDebtsByUserId(
        savedUser.getId());
    assertEquals(0, userCreditDebtsAfter.size(), "User should have 0 credit debts after deletion");
  }

  @Test
  void testCreateCreditDebtWithInvalidUser() {
    // Create a test business
    Business business = new Business();
    business.setName("Invalid Bank");
    Business savedBusiness = businessService.createBusiness(business);

    // Attempt to create a credit debt with a non-existent user ID
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setBusiness(savedBusiness);
    creditDebt.setAmount(new BigDecimal("1000.00"));
    creditDebt.setInterestRate(new BigDecimal("0.0499"));  // 4.99%

    UUID nonExistentUserId = UUID.randomUUID();

    // Assert that an exception is thrown
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      creditDebtService.createCreditDebt(creditDebt, nonExistentUserId);
    });

    String expectedMessage = "User not found with ID: " + nonExistentUserId;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage),
        "Exception message should contain the expected text");
  }
} */