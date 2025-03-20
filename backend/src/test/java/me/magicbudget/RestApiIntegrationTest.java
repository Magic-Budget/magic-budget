package me.magicbudget;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class RestApiIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

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


}