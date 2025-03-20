package me.magicbudget.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.magicbudget.dto.incomingrequest.LoginUserRequest;
import me.magicbudget.dto.incomingrequest.RegistrationAndAuthRequest;
import me.magicbudget.model.SavingGoal;
import me.magicbudget.model.UserInformation;
import me.magicbudget.model.User;
import me.magicbudget.security.jwt.JwtImplementationService;
import me.magicbudget.security.service.RegistrationAndAuthService;
import me.magicbudget.service.SavingGoalService;
import me.magicbudget.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class SavingGoalControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RegistrationAndAuthService registrationAndAuthService;

  @Autowired
  private UserService userService;

  @Autowired
  private SavingGoalService savingGoalService;

  @Autowired
  private JwtImplementationService jwtImplementationService;

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
  public void testGetSavingGoalByUserId() throws Exception {
    // Given
    SavingGoal savingGoal = new SavingGoal();
    savingGoal.setName("My Saving Goal");
    savingGoal.setAmount(BigDecimal.valueOf(1000));
    savingGoal.setUser(testUser);
    savingGoal = savingGoalService.createSavingGoal(savingGoal, testUser.getInformation().getId());
    List<SavingGoal> goals = savingGoalService.getSavingGoalsByUserId(testUser.getId());
    System.out.println("Saved goals: " + goals);

    //When
    mockMvc.perform(get("/api/saving-goals/" + savingGoal.getId())
            .header("Authorization", "Bearer " + bearerToken))
    // Then
        .andExpect(status().isOk())

        .andExpect(jsonPath("$.id").value(savingGoal.getId().toString()))
        .andExpect(jsonPath("$.name").value("My Saving Goal"))
        .andExpect(jsonPath("$.amount").value(1000))

        .andExpect(jsonPath("$.user.id").value(testUser.getInformation().getId().toString()))
        .andExpect(
            jsonPath("$.user.information.username").value(testUser.getInformation().getUsername()))
        .andExpect(jsonPath("$.user.information.firstName").value(
            testUser.getInformation().getFirstName()))
        .andExpect(
            jsonPath("$.user.information.lastName").value(testUser.getInformation().getLastName()))
        .andExpect(jsonPath("$.user.information.email").value(testUser.getInformation().getEmail()))

        .andExpect(jsonPath("$.user.information.enabled").value(true))
        .andExpect(jsonPath("$.user.information.credentialsNonExpired").value(true))
        .andExpect(jsonPath("$.user.information.accountNonExpired").value(true))
        .andExpect(jsonPath("$.user.information.accountNonLocked").value(true));
  }

  @Test
  public void testGetSavingGoalNotFound() throws Exception {
    // Given
    UUID nonExistentId = UUID.randomUUID();

    // When
    mockMvc.perform(get("/api/saving-goals/" + nonExistentId)
            .header("Authorization", "Bearer " + bearerToken))
    // Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateSavingGoal() throws Exception {
    // Given
    SavingGoal savingGoal = new SavingGoal();
    savingGoal.setName("Old Saving Goal");
    savingGoal.setAmount(BigDecimal.valueOf(1000));
    savingGoal.setUser(testUser);
    savingGoal = savingGoalService.createSavingGoal(savingGoal, testUser.getInformation().getId());

    // When
    Map<String, Object> updatedFields = new HashMap<>();
    updatedFields.put("name", "New Saving Goal");
    updatedFields.put("amount", 2000);

    String updatedJson = objectMapper.writeValueAsString(updatedFields);

    mockMvc.perform(put("/api/saving-goals/" + savingGoal.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedJson))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(savingGoal.getId().toString()))
        .andExpect(jsonPath("$.name").value("New Saving Goal"))
        .andExpect(jsonPath("$.amount").value(2000));
  }

  @Test
  public void testUpdateSavingGoalNotFound() throws Exception {
    // Given
    UUID nonExistentId = UUID.randomUUID();

    // When
    mockMvc.perform(get("/api/saving-goals/" + nonExistentId)
            .header("Authorization", "Bearer " + bearerToken))
    // Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testCreateSavingGoalForUser() throws Exception {
    // Given
    Map<String, Object> newSavingGoal = new HashMap<>();
    newSavingGoal.put("name", "New Saving Goal");
    newSavingGoal.put("amount", 1500);  // Assuming the endpoint accepts this number directly

    String payloadJson = objectMapper.writeValueAsString(newSavingGoal);

    // When
    mockMvc.perform(post("/api/saving-goals/" + testUser.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("New Saving Goal"))
        .andExpect(jsonPath("$.amount").value(1500))
        .andExpect(jsonPath("$.user.id").value(testUser.getId().toString()));
  }

  @Test
  public void testCreateSavingGoalForNonExistingUser() throws Exception {
    // Given
    UUID nonExistentId = UUID.randomUUID();

    Map<String, Object> newSavingGoal = new HashMap<>();
    newSavingGoal.put("name", "New Saving Goal");
    newSavingGoal.put("amount", 1500);  // Assuming the endpoint accepts this number directly

    String payloadJson = objectMapper.writeValueAsString(newSavingGoal);

    // When
    mockMvc.perform(post("/api/saving-goals/" + nonExistentId)
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteSavingGoal() throws Exception {
    // Given
    SavingGoal savingGoal = new SavingGoal();
    savingGoal.setName("Saving Goal");
    savingGoal.setAmount(BigDecimal.valueOf(1000));
    savingGoal.setUser(testUser);
    savingGoal = savingGoalService.createSavingGoal(savingGoal, testUser.getInformation().getId());

    // When
    mockMvc.perform(delete("/api/saving-goals/" + savingGoal.getId())
            .header("Authorization", "Bearer " + bearerToken))
        // Then
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteSavingGoalNotFound() throws Exception {
    // Given: A non-existent saving goal ID.
    UUID nonExistentId = UUID.randomUUID();

    // When/Then: A DELETE request should return 404 Not Found.
    mockMvc.perform(delete("/api/saving-goals/" + nonExistentId)
            .header("Authorization", "Bearer " + bearerToken))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetAllSavingGoalsOfUser() throws Exception {
    // Given
    SavingGoal savingGoal1 = new SavingGoal();
    savingGoal1.setName("Goal 1");
    savingGoal1.setAmount(BigDecimal.valueOf(1000));
    savingGoal1.setUser(testUser);
    savingGoal1 = savingGoalService.createSavingGoal(savingGoal1,
        testUser.getInformation().getId());

    SavingGoal savingGoal2 = new SavingGoal();
    savingGoal2.setName("Goal 2");
    savingGoal2.setAmount(BigDecimal.valueOf(2000));
    savingGoal2.setUser(testUser);
    savingGoal2 = savingGoalService.createSavingGoal(savingGoal2, testUser.getInformation().getId());

    //When
    mockMvc.perform(get("/api/saving-goals/user/" + testUser.getId())
            .header("Authorization", "Bearer " + bearerToken))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }
}