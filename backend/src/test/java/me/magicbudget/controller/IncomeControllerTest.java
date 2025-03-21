package me.magicbudget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.magicbudget.dto.incoming_request.IncomeRequest;
import me.magicbudget.dto.incoming_request.LoginUserRequest;
import me.magicbudget.dto.incoming_request.RegistrationAndAuthRequest;
import me.magicbudget.model.IncomeType;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import me.magicbudget.security.service.RegistrationAndAuthService;
import me.magicbudget.service.IncomeService;
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
public class IncomeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RegistrationAndAuthService registrationAndAuthService;

  @Autowired
  private UserService userService;

  @Autowired
  private IncomeService incomeService;

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

    LoginUserRequest loginUserRequest = new LoginUserRequest(
        username,
        password
    );

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
  public void testAddIncome() throws Exception {
    // Given
    //Prepare payload for new Income
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", "Salary");
    payload.put("description", "Monthly salary");
    payload.put("amount", 5000);
    payload.put("type", "MONTHLY");
    payload.put("date", "2025-03-21T00:47:50.133Z");

    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/" + testUser.getId() + "/income/add-income")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isCreated());
  }

  @Test
  public void testAddIncomeNonExistentUser() throws Exception {
    // Given: Generate a non-existent user ID.
    UUID nonExistentUserId = UUID.randomUUID();

    // Prepare the payload for the new income.
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", "Lottery");
    payload.put("description", "Won lottery, Long live life");
    payload.put("amount", 500000);
    payload.put("type", "ONETIME");
    payload.put("date", "2025-03-21T00:47:50.133Z");

    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/" + nonExistentUserId + "/income/add-income")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isForbidden());
  }

  @Test
  public void testGetIncomeForUser() throws Exception {
    // Given
    // Create two Income objects for same user

    // Create first income.
    IncomeRequest income1 = new IncomeRequest(
        "Salary",
        "Monthly salary",
        BigDecimal.valueOf(5000),
        IncomeType.MONTHLY,
        LocalDateTime.of(2025, 3, 21, 12, 30, 0));
    incomeService.addIncome(testUser.getId().toString(),income1);

    // Create second income (optional, to test multiple elements).
    IncomeRequest income2 = new IncomeRequest(
        "Lottery",
        "Won Lottery Viva la Vida",
        BigDecimal.valueOf(500000),
        IncomeType.ONETIME,
        LocalDateTime.of(2025, 3, 21, 12, 30, 0));
    incomeService.addIncome(testUser.getId().toString(),income2);

    // When
    mockMvc.perform(get("/api/" + testUser.getId() + "/income/view-all")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  public void testGetIncomeForUserEmpty() throws Exception {
    // Given
    // No Income for the user

    // When
    mockMvc.perform(get("/api/" + testUser.getId() + "/income/view-all")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

}
