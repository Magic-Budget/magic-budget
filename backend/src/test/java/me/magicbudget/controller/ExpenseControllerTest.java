package me.magicbudget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.magicbudget.dto.incomingrequest.ExpenseRequest;
import me.magicbudget.dto.incomingrequest.LoginUserRequest;
import me.magicbudget.dto.incomingrequest.RegistrationAndAuthRequest;
import me.magicbudget.model.Expense;
import me.magicbudget.model.ExpenseCategory;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import me.magicbudget.security.service.RegistrationAndAuthService;
import me.magicbudget.service.ExpenseService;
import me.magicbudget.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ExpenseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RegistrationAndAuthService registrationAndAuthService;

  @Autowired
  private UserService userService;

  @Autowired
  private ExpenseService expenseService;

  private User testUser;
  private String bearerToken;

  @BeforeEach
  public void setup() {
    String username = "testusername";
    String password = "test123";

    RegistrationAndAuthRequest registrationAndAuthRequest = new RegistrationAndAuthRequest(
        username,
        password,
        "Pancho",
        "Pantera",
        "panchito@test.com"
    );

    LoginUserRequest loginUserRequest = new LoginUserRequest(username, password);

    // Persist the User entity into the in-memory H2 database
    if (registrationAndAuthService.registerUser(registrationAndAuthRequest)) {
      Optional<UserInformation> userInformation = userService.getUserByUsername(username);
      if (userInformation.isPresent()) {
        Optional<User> optionalUser = userService.getUserById(userInformation.get().getId());
        if (optionalUser.isPresent()) {
          testUser = optionalUser.get();
          // Generate a JWT token for the test user.
          bearerToken = registrationAndAuthService.authenticate(loginUserRequest);
        }
      }
    }
  }

  @Test
  public void testAddExpense() throws Exception {
    // Given: Prepare payload for new Expense
    Map<String, Object> payload = new HashMap<>();
    payload.put("expenseName", "Groceries");
    payload.put("expenseDescription", "Weekly groceries");
    payload.put("amount", 150);
    payload.put("expenseCategory", "GROCERY");
    payload.put("expenseDate", "2025-03-21T00:47:50.133Z");
    payload.put("shopName", "Soriana");

    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/" + testUser.getId() + "/expense/add-expense")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isCreated());
  }

  @Test
  public void testAddExpenseNonExistentUser() throws Exception {
    // Given: Generate a non-existent user ID.
    UUID nonExistentUserId = UUID.randomUUID();

    Map<String, Object> payload = new HashMap<>();
    payload.put("expenseName", "Rent");
    payload.put("expenseDescription", "Monthly rent");
    payload.put("amount", 2300);
    payload.put("expenseCategory", "HEALTH");
    payload.put("expenseDate", "2025-03-21T00:47:50.133Z");
    payload.put("shopName", "Landlord");

    String payloadJson = objectMapper.writeValueAsString(payload);

    // When: Perform POST request to /api/{nonExistentUserId}/expense/add-expense
    mockMvc.perform(post("/api/" + nonExistentUserId + "/expense/add-expense")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then: Expect a 403 Forbidden response.
        .andExpect(status().isForbidden());
  }

  @Test
  public void testGetExpenseForUser() throws Exception {
    // Given
    // Create two Expense objects for the same user.
    ExpenseRequest expense1 = new ExpenseRequest(
        BigDecimal.valueOf(150),
        LocalDateTime.of(2025, 3, 21, 12, 30, 0),
        "Weekly groceries",
        ExpenseCategory.GROCERY,
        "Have to eat",
        "Soriana"
    );
    expenseService.addExpense(testUser.getId().toString(), expense1);

    ExpenseRequest expense2 = new ExpenseRequest(
        BigDecimal.valueOf(1500),
        LocalDateTime.of(2025, 3, 21, 12, 30, 0),
        "Electricity Bill",
        ExpenseCategory.HEALTH,
        "Need light in my life",
        "Manitoba Hydro"
    );
    expenseService.addExpense(testUser.getId().toString(), expense2);

    // When
    mockMvc.perform(get("/api/" + testUser.getId() + "/expense/view-all")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  public void testGetExpenseForUserEmpty() throws Exception {
    // Given
    //No Expense records exist for testUser.

    // When
    mockMvc.perform(get("/api/" + testUser.getId() + "/expense/view-all")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }
}

