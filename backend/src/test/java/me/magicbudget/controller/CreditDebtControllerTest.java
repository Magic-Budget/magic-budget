package me.magicbudget.controller;

import jakarta.transaction.Transactional;
import me.magicbudget.dto.incoming_request.LoginUserRequest;
import me.magicbudget.dto.incoming_request.RegistrationAndAuthRequest;
import me.magicbudget.model.Business;
import me.magicbudget.model.CreditDebt;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import me.magicbudget.security.service.RegistrationAndAuthService;
import me.magicbudget.service.BusinessService;
import me.magicbudget.service.CreditDebtService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class CreditDebtControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RegistrationAndAuthService registrationAndAuthService;

  @Autowired
  private UserService userService;

  @Autowired
  private BusinessService businessService;

  @Autowired
  private CreditDebtService creditDebtService;

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
  public void testGetCreditDebtById() throws Exception {
    // Given
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setAmount(BigDecimal.valueOf(1000));
    creditDebt.setInterestRate(BigDecimal.valueOf(5));

    //Create business associated to the credit debt
    Business business = new Business();
    business.setName("Test Business");
    business.setUser(testUser);
    businessService.createBusiness(business);

    creditDebt.setBusiness(business);
    creditDebt.setUser(testUser);
    creditDebt = creditDebtService.createCreditDebt(creditDebt,testUser.getInformation().getId());

    // When
    mockMvc.perform(get("/api/credit-debts/" + creditDebt.getId())
            .header("Authorization", "Bearer " + bearerToken))
        .andExpect(status().isOk())
        // Then
        .andExpect(jsonPath("$.id").value(creditDebt.getId().toString()))
        .andExpect(jsonPath("$.amount").value(creditDebt.getAmount().intValue()))
        .andExpect(jsonPath("$.interestRate").value(creditDebt.getInterestRate().intValue()))
        .andExpect(jsonPath("$.business.id").value(creditDebt.getBusiness().getId().toString()))
        .andExpect(jsonPath("$.business.name").value(creditDebt.getBusiness().getName()))
        .andExpect(jsonPath("$.business.user.id").value(creditDebt.getBusiness().getUser().getId().toString()))
        .andExpect(jsonPath("$.business.user.information.username")
            .value(creditDebt.getBusiness().getUser().getInformation().getUsername()))
        .andExpect(jsonPath("$.business.user.information.firstName")
            .value(creditDebt.getBusiness().getUser().getInformation().getFirstName()))
        .andExpect(jsonPath("$.business.user.information.lastName")
            .value(creditDebt.getBusiness().getUser().getInformation().getLastName()))
        .andExpect(jsonPath("$.user.id").value(creditDebt.getUser().getId().toString()))
        .andExpect(jsonPath("$.user.information.username")
            .value(creditDebt.getUser().getInformation().getUsername()))
        .andExpect(jsonPath("$.user.information.firstName")
            .value(creditDebt.getUser().getInformation().getFirstName()))
        .andExpect(jsonPath("$.user.information.lastName")
            .value(creditDebt.getUser().getInformation().getLastName()));
  }

  @Test
  public void testGetNonExistentCreditDebt() throws Exception {
    //Given
    UUID nonExistentId = UUID.randomUUID();

    //When
    mockMvc.perform(get("/api/credit-debts/" + nonExistentId)
        .header("Authorization","Bearer " + bearerToken))
    // Then
    .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateCreditDebt() throws Exception {
    // Given
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setAmount(BigDecimal.valueOf(1000));
    creditDebt.setInterestRate(BigDecimal.valueOf(5));

    //Create business associated to the credit debt
    Business business = new Business();
    business.setName("Test Business");
    business.setUser(testUser);
    businessService.createBusiness(business);

    creditDebt.setBusiness(business);
    creditDebt.setUser(testUser);
    creditDebt = creditDebtService.createCreditDebt(creditDebt,testUser.getInformation().getId());

    // For the update, create a new Business object
    Business newBusiness = new Business();
    newBusiness.setName("New Business");
    newBusiness.setUser(testUser);
    newBusiness = businessService.createBusiness(newBusiness);

    // Prepare update payload with new values
    Map<String, Object> updatePayload = new HashMap<>();
    updatePayload.put("businessId", newBusiness.getId().toString());
    updatePayload.put("userId", testUser.getId().toString());
    updatePayload.put("amount", 2000);
    updatePayload.put("interestRate", 10);

    String updateJson = objectMapper.writeValueAsString(updatePayload);

    // When
    mockMvc.perform(put("/api/credit-debts/" + creditDebt.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(creditDebt.getId().toString()))
        .andExpect(jsonPath("$.amount").value(2000))
        .andExpect(jsonPath("$.interestRate").value(10))
        .andExpect(jsonPath("$.business.id").value(newBusiness.getId().toString()))
        .andExpect(jsonPath("$.business.name").value("New Business"))
        .andExpect(jsonPath("$.business.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$.business.user.information.username")
            .value(testUser.getInformation().getUsername()))
        .andExpect(jsonPath("$.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$.user.information.username")
            .value(testUser.getInformation().getUsername()));
  }

  @Test
  public void testUpdateNonExistentCreditDebt() throws Exception {
    //Given
    UUID nonExistentId = UUID.randomUUID();

    // For the update, create a new Business object
    Business newBusiness = new Business();
    newBusiness.setName("New Business");
    newBusiness.setUser(testUser);
    newBusiness = businessService.createBusiness(newBusiness);

    // Prepare update payload with new values
    Map<String, Object> updatePayload = new HashMap<>();
    updatePayload.put("businessId", newBusiness.getId().toString());
    updatePayload.put("userId", testUser.getId().toString());
    updatePayload.put("amount", 2000);
    updatePayload.put("interestRate", 10);

    String updateJson = objectMapper.writeValueAsString(updatePayload);

    //When
    mockMvc.perform(put("/api/credit-debts/" + nonExistentId)
        .header("Authorization", "Bearer " + bearerToken)
        .header("X-User-Id", testUser.getId().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(updateJson))
    //Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateCreditDebtByInvalidUser() throws Exception {
    //Given
    UUID nonExistentId = UUID.randomUUID();

    // Given
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setAmount(BigDecimal.valueOf(1000));
    creditDebt.setInterestRate(BigDecimal.valueOf(5));

    //Create business associated to the credit debt
    Business business = new Business();
    business.setName("Test Business");
    business.setUser(testUser);
    businessService.createBusiness(business);

    creditDebt.setBusiness(business);
    creditDebt.setUser(testUser);
    creditDebt = creditDebtService.createCreditDebt(creditDebt,testUser.getInformation().getId());

    // For the update, create a new Business object
    Business newBusiness = new Business();
    newBusiness.setName("New Business");
    newBusiness.setUser(testUser);
    newBusiness = businessService.createBusiness(newBusiness);

    // Prepare update payload with new values
    Map<String, Object> updatePayload = new HashMap<>();
    updatePayload.put("businessId", newBusiness.getId().toString());
    updatePayload.put("userId", testUser.getId().toString());
    updatePayload.put("amount", 2000);
    updatePayload.put("interestRate", 10);

    String updateJson = objectMapper.writeValueAsString(updatePayload);

    //When
    mockMvc.perform(put("/api/credit-debts/" + nonExistentId)
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", nonExistentId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        //Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testCreateCreditDebt() throws Exception {
    // Given:
    // Create a new Business for the CreditDebt.
    Business newBusiness = new Business();
    newBusiness.setName("New Business");
    newBusiness.setUser(testUser);
    newBusiness = businessService.createBusiness(newBusiness);

    // Prepare the creation payload with the new business ID, test user's ID, amount, and interest rate.
    Map<String, Object> creationPayload = new HashMap<>();
    creationPayload.put("businessId", newBusiness.getId().toString());
    creationPayload.put("userId", testUser.getId().toString());
    creationPayload.put("amount", 1500);
    creationPayload.put("interestRate", 7);

    String creationJson = objectMapper.writeValueAsString(creationPayload);

    // When:
    mockMvc.perform(post("/api/credit-debts/" + testUser.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(creationJson))
        // Then:
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.amount").value(1500))
        .andExpect(jsonPath("$.interestRate").value(7))
        .andExpect(jsonPath("$.business.id").value(newBusiness.getId().toString()))
        .andExpect(jsonPath("$.business.name").value("New Business"))
        .andExpect(jsonPath("$.business.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$.user.information.username")
            .value(testUser.getInformation().getUsername()));
  }

  @Test
  public void testCreateCreditDebtNonExistentUser() throws Exception {
    // Given:
    UUID nonExistentUserId = UUID.randomUUID();

    // Create a new Business for the payload.
    Business newBusiness = new Business();
    newBusiness.setName("New Business");
    newBusiness.setUser(testUser);
    newBusiness = businessService.createBusiness(newBusiness);

    // Prepare payload
    Map<String, Object> creationPayload = new HashMap<>();
    creationPayload.put("businessId", newBusiness.getId().toString());
    creationPayload.put("userId", nonExistentUserId.toString());
    creationPayload.put("amount", 1500);
    creationPayload.put("interestRate", 7);

    String creationJson = objectMapper.writeValueAsString(creationPayload);

    // When:
    mockMvc.perform(post("/api/credit-debts/" + nonExistentUserId)
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", nonExistentUserId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(creationJson))
        // Then:
        .andExpect(status().isNotFound());
  }

  @Test
  public void testCreateCreditDebtByInvalidUser() throws Exception {
    // Given:
    // Create a new Business for the CreditDebt.
    Business newBusiness = new Business();
    newBusiness.setName("New Business");
    newBusiness.setUser(testUser);
    newBusiness = businessService.createBusiness(newBusiness);

    // Prepare payload using testUser's ID.
    Map<String, Object> creationPayload = new HashMap<>();
    creationPayload.put("businessId", newBusiness.getId().toString());
    creationPayload.put("userId", testUser.getId().toString());
    creationPayload.put("amount", 1500);
    creationPayload.put("interestRate", 7);

    String creationJson = objectMapper.writeValueAsString(creationPayload);

    // Use an invalid user ID in the header (simulate a mismatch).
    UUID invalidUserId = UUID.randomUUID();

    // When:
    mockMvc.perform(post("/api/credit-debts/" + testUser.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", invalidUserId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(creationJson))
        // Then:
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteCreditDebt() throws Exception {
    // Given
    CreditDebt creditDebt = new CreditDebt();
    creditDebt.setAmount(BigDecimal.valueOf(1000));
    creditDebt.setInterestRate(BigDecimal.valueOf(5));

    //Create business associated to the credit debt
    Business business = new Business();
    business.setName("Test Business");
    business.setUser(testUser);
    businessService.createBusiness(business);

    creditDebt.setBusiness(business);
    creditDebt.setUser(testUser);
    creditDebt = creditDebtService.createCreditDebt(creditDebt,testUser.getInformation().getId());

    //When
    mockMvc.perform(delete("/api/credit-debts/" + creditDebt.getId())
        .header("Authorization", "Bearer " + bearerToken))
    // Then
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteNonExistentCreditDebt() throws Exception {
    // Given
    UUID nonExistentId = UUID.randomUUID();

    //When
    mockMvc.perform(delete("/api/credit-debts/" + nonExistentId)
            .header("Authorization", "Bearer " + bearerToken))
        // Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetCreditDebtsForUser() throws Exception {
    // Given:
    // Create two CreditDebts for same user
    // Create first CreditDebt with its associated Business.
    Business business1 = new Business();
    business1.setName("Business One");
    business1.setUser(testUser);
    business1 = businessService.createBusiness(business1);

    CreditDebt creditDebt1 = new CreditDebt();
    creditDebt1.setAmount(BigDecimal.valueOf(1000));
    creditDebt1.setInterestRate(BigDecimal.valueOf(5));
    creditDebt1.setBusiness(business1);
    creditDebt1.setUser(testUser);
    creditDebt1 = creditDebtService.createCreditDebt(creditDebt1, testUser.getInformation().getId());

    // Create second CreditDebt with its associated Business.
    Business business2 = new Business();
    business2.setName("Business Two");
    business2.setUser(testUser);
    business2 = businessService.createBusiness(business2);

    CreditDebt creditDebt2 = new CreditDebt();
    creditDebt2.setAmount(BigDecimal.valueOf(2000));
    creditDebt2.setInterestRate(BigDecimal.valueOf(7));
    creditDebt2.setBusiness(business2);
    creditDebt2.setUser(testUser);
    creditDebt2 = creditDebtService.createCreditDebt(creditDebt2, testUser.getInformation().getId());

    // When
    mockMvc.perform(get("/api/credit-debts/user/" + testUser.getId())
            .header("Authorization", "Bearer " + bearerToken))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        // Validate fields for the credit debts
        .andExpect(jsonPath("$[0].id").value(creditDebt1.getId().toString()))
        .andExpect(jsonPath("$[0].amount").value(creditDebt1.getAmount().intValue()))
        .andExpect(jsonPath("$[0].interestRate").value(creditDebt1.getInterestRate().intValue()))
        .andExpect(jsonPath("$[0].business.id").value(business1.getId().toString()))
        .andExpect(jsonPath("$[0].business.name").value("Business One"))
        .andExpect(jsonPath("$[0].business.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$[0].user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$[0].user.information.username")
            .value(testUser.getInformation().getUsername()))
        .andExpect(jsonPath("$[1].id").value(creditDebt2.getId().toString()))
        .andExpect(jsonPath("$[1].amount").value(creditDebt2.getAmount().intValue()))
        .andExpect(jsonPath("$[1].interestRate").value(creditDebt2.getInterestRate().intValue()))
        .andExpect(jsonPath("$[1].business.id").value(business2.getId().toString()))
        .andExpect(jsonPath("$[1].business.name").value("Business Two"))
        .andExpect(jsonPath("$[1].business.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$[1].user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$[1].user.information.username")
            .value(testUser.getInformation().getUsername()));
  }

  @Test
  public void testGetCreditDebtsForUserEmpty() throws Exception {
    // Given
    // No credit debts for the given user
    creditDebtService.deleteAllCreditDebtsByUserId(testUser.getId());

    // When
    mockMvc.perform(get("/api/credit-debts/user/" + testUser.getId())
            .header("Authorization", "Bearer " + bearerToken))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  public void testDeleteAllCreditDebtsForUser() throws Exception {
    // Given:
    // Create two CreditDebts for same user
    // Create first CreditDebt with its associated Business.
    Business business1 = new Business();
    business1.setName("Business One");
    business1.setUser(testUser);
    business1 = businessService.createBusiness(business1);

    CreditDebt creditDebt1 = new CreditDebt();
    creditDebt1.setAmount(BigDecimal.valueOf(1000));
    creditDebt1.setInterestRate(BigDecimal.valueOf(5));
    creditDebt1.setBusiness(business1);
    creditDebt1.setUser(testUser);
    creditDebt1 = creditDebtService.createCreditDebt(creditDebt1, testUser.getInformation().getId());

    // Create second CreditDebt with its associated Business.
    Business business2 = new Business();
    business2.setName("Business Two");
    business2.setUser(testUser);
    business2 = businessService.createBusiness(business2);

    CreditDebt creditDebt2 = new CreditDebt();
    creditDebt2.setAmount(BigDecimal.valueOf(2000));
    creditDebt2.setInterestRate(BigDecimal.valueOf(7));
    creditDebt2.setBusiness(business2);
    creditDebt2.setUser(testUser);
    creditDebt2 = creditDebtService.createCreditDebt(creditDebt2, testUser.getInformation().getId());

    // When
    mockMvc.perform(delete("/api/credit-debts/user/" + testUser.getId())
            .header("Authorization", "Bearer " + bearerToken))
        // Then
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteAllCreditDebtsForUserEmpty() throws Exception {
    // Given:
    // No credit debts for the given user
    creditDebtService.deleteAllCreditDebtsByUserId(testUser.getId());

    // When
    mockMvc.perform(delete("/api/credit-debts/user/" + testUser.getId())
            .header("Authorization", "Bearer " + bearerToken))
        // Then
        .andExpect(status().isNoContent());
  }
}
