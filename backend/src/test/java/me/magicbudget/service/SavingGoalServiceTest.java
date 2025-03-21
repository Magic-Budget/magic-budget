// This code was generated with Claud 3.7 Thinker inside VScode with
// the prompt "Create a test class for the SavingGoalService class".
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
import me.magicbudget.model.SavingGoal;
import me.magicbudget.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SavingGoalServiceTest {

  @Autowired
  private SavingGoalService savingGoalService;

  @Autowired
  private UserService userService;

  @Test
  void testCreateSavingGoal() {
    // Create a new user
    User user = new User(null, "savinguser1", "John", "Doe", "password123");
    User savedUser = userService.createUser(user);

    // Create a saving goal
    SavingGoal goal = new SavingGoal();
    goal.setName("New Car");
    goal.setAmount(new BigDecimal("5000.00"));

    // Save the goal
    SavingGoal savedGoal = savingGoalService.createSavingGoal(goal, savedUser.getId());

    // Assertions
    assertNotNull(savedGoal.getId(), "Saving goal ID should not be null");
    assertEquals("New Car", savedGoal.getName(), "Goal name should match");
    assertEquals(new BigDecimal("5000.00"), savedGoal.getAmount(), "Goal amount should match");
    assertEquals(savedUser.getId(), savedGoal.getUser().getId(), "User ID should match");
  }

  @Test
  void testGetSavingGoalById() {
    // Create a new user
    User user = new User(null, "savinguser2", "Jane", "Smith", "password456");
    User savedUser = userService.createUser(user);

    // Create and save a saving goal
    SavingGoal goal = new SavingGoal();
    goal.setName("Vacation");
    goal.setAmount(new BigDecimal("2500.50"));
    SavingGoal savedGoal = savingGoalService.createSavingGoal(goal, savedUser.getId());

    // Retrieve the goal
    Optional<SavingGoal> retrievedGoalOpt = savingGoalService.getSavingGoalById(savedGoal.getId());

    // Assertions
    assertTrue(retrievedGoalOpt.isPresent(), "Saving goal should be present");
    SavingGoal retrievedGoal = retrievedGoalOpt.get();
    assertEquals(savedGoal.getId(), retrievedGoal.getId(), "Goal ID should match");
    assertEquals("Vacation", retrievedGoal.getName(), "Goal name should match");
    assertEquals(savedUser.getId(), retrievedGoal.getUser().getId(), "User ID should match");
  }

  @Test
  void testGetSavingGoalsByUserId() {
    // Create a new user
    User user = new User(null, "savinguser3", "Bob", "Johnson", "password789");
    User savedUser = userService.createUser(user);

    // Create and save multiple saving goals
    SavingGoal goal1 = new SavingGoal();
    goal1.setName("Emergency Fund");
    goal1.setAmount(new BigDecimal("10000.00"));
    savingGoalService.createSavingGoal(goal1, savedUser.getId());

    SavingGoal goal2 = new SavingGoal();
    goal2.setName("New Computer");
    goal2.setAmount(new BigDecimal("1500.00"));
    savingGoalService.createSavingGoal(goal2, savedUser.getId());

    // Retrieve goals for the user
    List<SavingGoal> userGoals = savingGoalService.getSavingGoalsByUserId(savedUser.getId());

    // Assertions
    assertEquals(2, userGoals.size(), "User should have 2 saving goals");
    assertTrue(userGoals.stream().anyMatch(g -> g.getName().equals("Emergency Fund")),
        "Should contain Emergency Fund goal");
    assertTrue(userGoals.stream().anyMatch(g -> g.getName().equals("New Computer")),
        "Should contain New Computer goal");
  }

  @Test
  void testUpdateSavingGoal() {
    // Create a new user
    User user = new User(null, "savinguser4", "Alice", "Brown", "password101");
    User savedUser = userService.createUser(user);

    // Create and save a saving goal
    SavingGoal goal = new SavingGoal();
    goal.setName("Home Renovation");
    goal.setAmount(new BigDecimal("15000.00"));
    SavingGoal savedGoal = savingGoalService.createSavingGoal(goal, savedUser.getId());

    // Update the goal
    savedGoal.setAmount(new BigDecimal("20000.00"));
    savedGoal.setName("Complete Home Renovation");
    SavingGoal updatedGoal = savingGoalService.updateSavingGoal(savedGoal);

    // Assertions
    assertEquals("Complete Home Renovation", updatedGoal.getName(), "Updated name should match");
    assertEquals(new BigDecimal("20000.00"), updatedGoal.getAmount(), "Updated amount should match");
  }

  @Test
  void testDeleteSavingGoal() {
    // Create a new user
    User user = new User(null, "savinguser5", "Tom", "Wilson", "password202");
    User savedUser = userService.createUser(user);

    // Create and save a saving goal
    SavingGoal goal = new SavingGoal();
    goal.setName("Wedding");
    goal.setAmount(new BigDecimal("8000.00"));
    SavingGoal savedGoal = savingGoalService.createSavingGoal(goal, savedUser.getId());

    // Delete the goal
    savingGoalService.deleteSavingGoal(savedGoal.getId());

    // Try to retrieve the deleted goal
    Optional<SavingGoal> deletedGoal = savingGoalService.getSavingGoalById(savedGoal.getId());

    // Assertions
    assertFalse(deletedGoal.isPresent(), "Goal should be deleted");
  }

  @Test
  void testDeleteAllSavingGoalsByUserId() {
    // Create a new user
    User user = new User(null, "savinguser6", "Emily", "Davis", "password303");
    User savedUser = userService.createUser(user);

    // Create and save multiple saving goals
    SavingGoal goal1 = new SavingGoal();
    goal1.setName("College Fund");
    goal1.setAmount(new BigDecimal("25000.00"));
    savingGoalService.createSavingGoal(goal1, savedUser.getId());

    SavingGoal goal2 = new SavingGoal();
    goal2.setName("New Phone");
    goal2.setAmount(new BigDecimal("1000.00"));
    savingGoalService.createSavingGoal(goal2, savedUser.getId());

    // Verify goals exist
    List<SavingGoal> userGoalsBefore = savingGoalService.getSavingGoalsByUserId(savedUser.getId());
    assertEquals(2, userGoalsBefore.size(), "User should have 2 saving goals initially");

    // Delete all goals for the user
    savingGoalService.deleteAllSavingGoalsByUserId(savedUser.getId());

    // Verify goals are deleted
    List<SavingGoal> userGoalsAfter = savingGoalService.getSavingGoalsByUserId(savedUser.getId());
    assertEquals(0, userGoalsAfter.size(), "User should have 0 saving goals after deletion");
  }

  @Test
  void testCreateSavingGoalWithInvalidUser() {
    // Attempt to create a saving goal with a non-existent user ID
    SavingGoal goal = new SavingGoal();
    goal.setName("Invalid Goal");
    goal.setAmount(new BigDecimal("1000.00"));

    UUID nonExistentUserId = UUID.randomUUID();

    // Assert that an exception is thrown
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      savingGoalService.createSavingGoal(goal, nonExistentUserId);
    });

    String expectedMessage = "User not found with ID: " + nonExistentUserId;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage),
        "Exception message should contain the expected text");
  }
}*/