package me.magicbudget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.magicbudget.dto.incoming_request.LoginUserRequest;
import me.magicbudget.dto.incoming_request.RegistrationAndAuthRequest;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import me.magicbudget.security.service.RegistrationAndAuthService;
import me.magicbudget.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RegistrationAndAuthService registrationAndAuthService;

  @Autowired
  private UserService userService;

  private final String username = "testusername";
  private final String password = "test123";

  @BeforeEach
  public void setup() {
    String username = "testusername";
    String password = "test123";

    // Register a test user.
    RegistrationAndAuthRequest registrationRequest = new RegistrationAndAuthRequest(
        username,
        password,
        "Pancho",
        "Pantera",
        "panchito@test.com"
    );
    registrationAndAuthService.registerUser(registrationRequest);
  }

  @Test
  public void testSignIn() throws Exception {
    // Given
    //Prepare payload with existing user
    Map<String, String> payload = new HashMap<>();
    payload.put("username", username);
    payload.put("password", password);
    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").exists())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.firstName").value("Pancho"))
        .andExpect(jsonPath("$.lastName").value("Pantera"));
  }

  @Test
  public void testSignInNonExistentUser() throws Exception {
    // Given
    //Prepare payload with non-existing user
    Map<String, String> payload = new HashMap<>();
    payload.put("username", "NonExistentUser");
    payload.put("password", "NonExistentPassword");
    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testSignInWithIncorrectPassword() throws Exception {
    // Given
    //Prepare payload with existing user but incorrect password
    Map<String, String> payload = new HashMap<>();
    payload.put("username", username);
    payload.put("password", "BadPassword");
    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isUnauthorized());
  }

  /*
  @Test
  public void testRegister() throws Exception {
    // Given
    // Prepare the registration payload with new user
    Map<String, String> payload = new HashMap<>();
    payload.put("username", "newuser");
    payload.put("password", "password456");
    payload.put("firstName", "John");
    payload.put("lastName", "Doe");
    payload.put("email", "john.doe@example.com");

    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").exists())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"));
  }

  @Test
  public void testRegisterBadInput() throws Exception {
    // Given
    // Prepare the registration payload with empty fields
    Map<String, String> payload = new HashMap<>();
    payload.put("username", "");
    payload.put("password", "");
    payload.put("firstName", "John");
    payload.put("lastName", "Doe");
    payload.put("email", "john.doe@example.com");

    String payloadJson = objectMapper.writeValueAsString(payload);

    // When
    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payloadJson))
        // Then
        .andExpect(status().isUnauthorized());
  }
  */
}

