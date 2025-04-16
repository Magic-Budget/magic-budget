package me.magicbudget.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import me.magicbudget.security.service.RegistrationAndAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

  @Autowired
  private UserService userService;
  @Autowired
  private RegistrationAndAuthService registrationAndAuthService;

  @Test
  void testCreateUser() {

    var user = registrationAndAuthService.createUser(
        new UserInformation("testuser", "hashedPass123", "Bruce", "Wayne", ""));

    var savedUser = userService.createUser(user);

    assertNotNull(savedUser.getId(), "User ID should not be null after saving");
    assertEquals("testuser", savedUser.getInformation().getUsername(), "Username should match");
  }

  @Test
  void testGetUserById() {
    var user = registrationAndAuthService.createUser(
        new UserInformation("testuser2", "hashedPass123", "Bruce", "Wayne", ""));

    var savedUser = userService.createUser(user);

    Optional<User> retrievedUserOpt = userService.getUserById(savedUser.getId());

    assertTrue(retrievedUserOpt.isPresent(), "User should be present");
    User retrievedUser = retrievedUserOpt.get();
    assertEquals(savedUser.getId(), retrievedUser.getId(), "IDs should match");
    assertEquals("testuser2", retrievedUser.getInformation().getUsername(),
        "Username should match");
  }

  @Test
  void testGetUserByUsername() {
    var user = registrationAndAuthService.createUser(
        new UserInformation("testuser3", "hashedPass789", "Diana", "Prince", ""));

    userService.createUser(user);

    Optional<UserInformation> retrievedUserOpt = userService.getUserByUsername("testuser3");

    assertTrue(retrievedUserOpt.isPresent(), "User should be present");
  }

  @Test
  void testUpdateUser() {
    var user = registrationAndAuthService.createUser(
        new UserInformation("testuser4", "hashedPass000", "Barry", "Allen", ""));
    var savedUser = userService.createUser(user);

    savedUser.setInformation(new UserInformation("testuser4", "hashedPass", "Barry", "Allen", ""));
    var updatedUser = userService.updateUser(savedUser);

    assertEquals("hashedPass", updatedUser.getInformation().getPassword(),
        "Password should be updated");
  }
}