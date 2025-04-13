package me.magicbudget.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import me.magicbudget.model.Goal;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GoalServiceTest {

    @Autowired
    private GoalService goalService;
    
    @Autowired
    private UserService userService;
    
    private User createTestUser() {
        var userInfo = new UserInformation("goaluser", "password123", "John", "Doe", "");
        return userService.createUser(new User(userInfo));
    }
    
    @Test
    void testCreateGoal() {
        User user = createTestUser();
        
        BigDecimal targetAmount = new BigDecimal("1000.00");
        BigDecimal currentAmount = new BigDecimal("250.00");
        LocalDate dueDate = LocalDate.now().plusMonths(3);
        
        Optional<Goal> goalOpt = goalService.createGoal(user.getId(), "Vacation Fund", targetAmount, currentAmount, dueDate);
        
        assertTrue(goalOpt.isPresent(), "Goal should be created");
        Goal goal = goalOpt.get();
        
        assertNotNull(goal.id(), "Goal ID should not be null");
        assertEquals("Vacation Fund", goal.name(), "Goal name should match");
        assertEquals(targetAmount, goal.target(), "Target amount should match");
        assertEquals(currentAmount, goal.currentAmount(), "Current amount should match");
        assertEquals(dueDate, goal.due(), "Due date should match");
        assertEquals(user.getId(), goal.user().getId(), "User ID should match");
    }
    
    @Test
    void testGetGoalById() {
        User user = createTestUser();
        
        BigDecimal targetAmount = new BigDecimal("2000.00");
        BigDecimal currentAmount = new BigDecimal("500.00");
        LocalDate dueDate = LocalDate.now().plusMonths(6);
        
        Goal createdGoal = goalService.createGoal(user.getId(), "Car Fund", targetAmount, currentAmount, dueDate).orElseThrow();
        
        Optional<Goal> retrievedGoalOpt = goalService.getGoalById(createdGoal.id());
        
        assertTrue(retrievedGoalOpt.isPresent(), "Goal should be found by ID");
        Goal retrievedGoal = retrievedGoalOpt.get();
        
        assertEquals(createdGoal.id(), retrievedGoal.id(), "Goal ID should match");
        assertEquals("Car Fund", retrievedGoal.name(), "Goal name should match");
    }
    
    @Test
    void testGetGoalsByUserId() {
        User user = createTestUser();
        
        goalService.createGoal(user.getId(), "Vacation Fund", new BigDecimal("1000.00"), new BigDecimal("250.00"), LocalDate.now().plusMonths(3));
        goalService.createGoal(user.getId(), "Car Fund", new BigDecimal("5000.00"), new BigDecimal("1000.00"), LocalDate.now().plusMonths(12));
        
        List<Goal> goals = goalService.getGoalsByUserId(user.getId());
        
        assertNotNull(goals, "Goals list should not be null");
        assertEquals(2, goals.size(), "User should have 2 goals");
        
        for (Goal goal : goals) {
            assertEquals(user.getId(), goal.user().getId(), "Goal should belong to the user");
        }
    }
    
    @Test
    void testUpdateGoal() {
        User user = createTestUser();
        
        BigDecimal initialTargetAmount = new BigDecimal("3000.00");
        BigDecimal initialCurrentAmount = new BigDecimal("500.00");
        LocalDate initialDueDate = LocalDate.now().plusMonths(6);
        
        Goal createdGoal = goalService.createGoal(user.getId(), "Home Fund", initialTargetAmount, initialCurrentAmount, initialDueDate).orElseThrow();
        
        BigDecimal updatedTargetAmount = new BigDecimal("3500.00");
        BigDecimal updatedCurrentAmount = new BigDecimal("750.00");
        LocalDate updatedDueDate = LocalDate.now().plusMonths(8);
        
        Optional<Goal> updatedGoalOpt = goalService.updateGoal(createdGoal.id(), "New Home Fund", updatedTargetAmount, updatedCurrentAmount, updatedDueDate);
        
        assertTrue(updatedGoalOpt.isPresent(), "Updated goal should be present");
        Goal updatedGoal = updatedGoalOpt.get();
        
        assertEquals("New Home Fund", updatedGoal.name(), "Goal name should be updated");
        assertEquals(updatedTargetAmount, updatedGoal.target(), "Target amount should be updated");
        assertEquals(updatedCurrentAmount, updatedGoal.currentAmount(), "Current amount should be updated");
        assertEquals(updatedDueDate, updatedGoal.due(), "Due date should be updated");
    }
    
    @Test
    void testDeleteGoal() {
        User user = createTestUser();
        
        Goal createdGoal = goalService.createGoal(user.getId(), "Short-term Fund", new BigDecimal("500.00"), new BigDecimal("100.00"), LocalDate.now().plusMonths(1)).orElseThrow();
        
        goalService.deleteGoal(createdGoal.id());
        
        Optional<Goal> deletedGoalOpt = goalService.getGoalById(createdGoal.id());
        
        assertFalse(deletedGoalOpt.isPresent(), "Goal should be deleted");
    }
}
