package me.magicbudget.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.magicbudget.dto.incoming_request.LoginUserRequest;
import me.magicbudget.dto.incoming_request.RegistrationAndAuthRequest;
import me.magicbudget.model.Business;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import me.magicbudget.security.service.RegistrationAndAuthService;
import me.magicbudget.service.BusinessService;
import me.magicbudget.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class BusinessControllerTest {

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
  public void testGetBusinessById() throws Exception {
    // Given
    Business business = new Business();
    business.setName("Test Business");
    business.setUser(testUser);
    business = businessService.createBusiness(business);

    // When
    mockMvc.perform(get("/api/businesses/" + business.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(business.getId().toString()))
        .andExpect(jsonPath("$.name").value("Test Business"))
        .andExpect(jsonPath("$.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$.user.information.username")
            .value(testUser.getInformation().getUsername()))
        .andExpect(jsonPath("$.user.information.firstName")
            .value(business.getUser().getInformation().getFirstName()))
        .andExpect(jsonPath("$.user.information.lastName")
            .value(business.getUser().getInformation().getLastName()));
  }

  @Test
  public void testGetBusinessByIdNotFound() throws Exception {
    // Given
    UUID nonExistentBusinessId = UUID.randomUUID();

    // When
    mockMvc.perform(get("/api/businesses/" + nonExistentBusinessId)
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetBusinessByIdUnauthorized() throws Exception {
    // Given
    Business business = new Business();
    business.setName("Test Business");
    business.setUser(testUser);
    business = businessService.createBusiness(business);

    // When
    // Perform a GET request without the X-User-Id header.
    mockMvc.perform(get("/api/businesses/" + business.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateBusinessSuccess() throws Exception {
    // Given
    Business business = new Business();
    business.setName("Original Business");
    business.setUser(testUser);
    business = businessService.createBusiness(business);

    // Prepare payload
    Map<String, Object> updatePayload = new HashMap<>();
    updatePayload.put("id", business.getId().toString());
    updatePayload.put("name", "Updated Business");

    String updateJson = objectMapper.writeValueAsString(updatePayload);

    // When: Perform the PUT request.
    mockMvc.perform(put("/api/businesses/" + business.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(business.getId().toString()))
        .andExpect(jsonPath("$.name").value("Updated Business"))
        .andExpect(jsonPath("$.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$.user.information.username")
            .value(testUser.getInformation().getUsername()));
  }

  @Test
  public void testUpdateNonExistentBusiness() throws Exception {
    // Given
    UUID nonExistentBusinessId = UUID.randomUUID();

    // Prepare update payload
    Map<String, Object> updatePayload = new HashMap<>();
    updatePayload.put("id", nonExistentBusinessId.toString());
    updatePayload.put("name", "Updated Business");

    String updateJson = objectMapper.writeValueAsString(updatePayload);

    // When: Perform the PUT request using the non-existent business ID.
    mockMvc.perform(put("/api/businesses/" + nonExistentBusinessId)
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        // Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateBusinessUnauthorized() throws Exception {
    // Given
    Business business = new Business();
    business.setName("Original Business");
    business.setUser(testUser);
    business = businessService.createBusiness(business);

    // Prepare update payload
    Map<String, Object> updatePayload = new HashMap<>();
    updatePayload.put("id", business.getId().toString());
    updatePayload.put("name", "Updated Business");

    String updateJson = objectMapper.writeValueAsString(updatePayload);

    // When
    // Perform the PUT request without the X-User-Id header.
    mockMvc.perform(put("/api/businesses/" + business.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        // Then
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteBusiness() throws Exception {
    // Given
    Business business = new Business();
    business.setName("Original Business");
    business.setUser(testUser);
    business = businessService.createBusiness(business);

    // When
    mockMvc.perform(delete("/api/businesses/" + business.getId())
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteNonExistentBusiness() throws Exception {
    // Given
    UUID nonExistentBusinessId = UUID.randomUUID();

    // When
    mockMvc.perform(delete("/api/businesses/" + nonExistentBusinessId)
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetBusinessesForUser() throws Exception {
    // Given
    // Create two Business for the same user
    Business business1 = new Business();
    business1.setName("Business One");
    business1.setUser(testUser);
    business1 = businessService.createBusiness(business1);

    Business business2 = new Business();
    business2.setName("Business Two");
    business2.setUser(testUser);
    business2 = businessService.createBusiness(business2);

    // When
    mockMvc.perform(get("/api/businesses")
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(business1.getId().toString()))
        .andExpect(jsonPath("$[0].name").value("Business One"))
        .andExpect(jsonPath("$[0].user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$[0].user.information.username")
            .value(testUser.getInformation().getUsername()))
        .andExpect(jsonPath("$[1].id").value(business2.getId().toString()))
        .andExpect(jsonPath("$[1].name").value("Business Two"))
        .andExpect(jsonPath("$[1].user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$[1].user.information.username")
            .value(testUser.getInformation().getUsername()));
  }

  @Test
  public void testGetBusinessesForUserEmpty() throws Exception {
    // Given
    //No business for this user

    // When
    mockMvc.perform(get("/api/businesses")
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  public void testGetBusinessesForUserUnauthorized() throws Exception {
    // Given
    Business business = new Business();
    business.setName("Unauthorized Business");
    business.setUser(testUser);
    business = businessService.createBusiness(business);

    // When: Perform a GET request without the X-User-Id header.
    mockMvc.perform(get("/api/businesses")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON))
        // Then
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateBusiness() throws Exception {
    // Given
    //Prepare payload
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", "New Business");
    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/businesses")
            .header("Authorization", "Bearer " + bearerToken)
            .header("X-User-Id", testUser.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("New Business"))
        .andExpect(jsonPath("$.user.id").value(testUser.getId().toString()))
        .andExpect(jsonPath("$.user.information.username")
            .value(testUser.getInformation().getUsername()));
  }

  @Test
  public void testCreateBusiness_Unauthorized() throws Exception {
    // Given
    //Prepare payload
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", "New Business");
    String payloadJson = objectMapper.writeValueAsString(payload);

    // When:
    // Perform POST request to /api/businesses without the X-User-Id header
    mockMvc.perform(post("/api/businesses")
            .header("Authorization", "Bearer " + bearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isBadRequest());
  }

}
