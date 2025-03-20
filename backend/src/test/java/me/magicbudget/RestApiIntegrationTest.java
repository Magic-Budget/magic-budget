package me.magicbudget;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.transaction.Transactional;
import me.magicbudget.model.SavingGoal;
import me.magicbudget.model.UserInformation;
import me.magicbudget.model.User;
import me.magicbudget.security.jwt.JwtImplementationService;
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class RestApiIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

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

    UserInformation testUserInfo = new UserInformation(
        "testuser",               // username
        "test123",               // password (ensure it meets your encoding/validation requirements)
        "Pancho",                   // firstName
        "Pantera",                   // lastName
        "panchito@test.com"    // email
    );

    testUser = new User(testUserInfo);

    // Persist the User entity into the in-memory H2 database
    userService.createUser(testUser);

    // Generate a JWT token for the test user.
    bearerToken = jwtImplementationService.generateToken(new HashMap<>(), testUserInfo);
  }

  @Test
  public void testGetSavingGoalByUserId() throws Exception {
    // Create a saving goal for the test user
    SavingGoal savingGoal = new SavingGoal();
    savingGoal.setName("My Saving Goal");
    savingGoal.setAmount(BigDecimal.valueOf(1000));
    savingGoal.setUser(testUser);
    savingGoal = savingGoalService.createSavingGoal(savingGoal,testUser.getInformation().getId());

    //Perform request and verify answer
    mockMvc.perform(get("/api/saving-goals/" + testUser.getInformation().getId())
            .header("Authorization", "Bearer " + bearerToken))
        .andExpect(status().isOk())
        // Validate top-level saving goal fields
        .andExpect(jsonPath("$.id").value(savingGoal.getId().toString()))
        .andExpect(jsonPath("$.name").value("My Saving Goal"))
        .andExpect(jsonPath("$.amount").value(1000))
        // Validate the nested user structure
        .andExpect(jsonPath("$.user.id").value(testUser.getInformation().getId().toString()))
        .andExpect(jsonPath("$.user.information.username").value(testUser.getInformation().getUsername()))
        .andExpect(jsonPath("$.user.information.firstName").value(testUser.getInformation().getFirstName()))
        .andExpect(jsonPath("$.user.information.lastName").value(testUser.getInformation().getLastName()))
        .andExpect(jsonPath("$.user.information.email").value(testUser.getInformation().getEmail()))
        // Validate additional fields if they are part of your response
        .andExpect(jsonPath("$.user.information.enabled").value(true))
        .andExpect(jsonPath("$.user.information.credentialsNonExpired").value(true))
        .andExpect(jsonPath("$.user.information.accountNonExpired").value(true))
        .andExpect(jsonPath("$.user.information.accountNonLocked").value(true));
    // You can also add assertions for the authorities array if needed.
  }
}
