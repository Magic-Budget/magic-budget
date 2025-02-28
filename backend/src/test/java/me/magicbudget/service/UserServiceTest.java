// This code was generated with Claud 3.7 Thinker inside VScode with
// the prompt "Create a test class for the UserService class".
package me.magicbudget.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import me.magicbudget.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  void testCreateUser() {
    var user = new User(null, "testuser", "bruce", "wayne", "");

    var savedUser = userService.createUser(user);

    assertNotNull(savedUser.getId(), "User ID should not be null after saving");
    assertEquals("testuser", savedUser.getUsername(), "Username should match");

    System.out.println("Does this work?");
  }

  @Test
  void testGetUserByGetId() {
    User user = new User(
        null,
        "testuser2",
        "Clark",
        "Kent",
        "hashedPass456"
    );
    User savedUser = userService.createUser(user);

    Optional<User> retrievedUserOpt = userService.getUserById(savedUser.getId());

    assertTrue(retrievedUserOpt.isPresent(), "User should be present");
    User retrievedUser = retrievedUserOpt.get();
    assertEquals(savedUser.getId(), retrievedUser.getId());
    assertEquals("testuser2", retrievedUser.getUsername());
    assertEquals("Clark", retrievedUser.getFirstName());
  }


  @Test
  void testGetUserByGetUsername() {
    User user = new User(
        null,
        "testuser3",
        "Diana",
        "Prince",
        "hashedPass789"
    );
    userService.createUser(user);

    Optional<User> retrievedUserOpt = userService.getUserByUsername("testuser3");

    assertTrue(retrievedUserOpt.isPresent(), "User should be present");
    User retrievedUser = retrievedUserOpt.get();
    assertEquals("testuser3", retrievedUser.getUsername());
    assertEquals("Diana", retrievedUser.getFirstName());
    assertEquals("Prince", retrievedUser.getLastName());
    assertEquals("hashedPass789", retrievedUser.getHashedPassword());
  }

  @Test
  void testUpdateUser() {
    User user = new User(
        null,
        "testuser4",
        "Barry",
        "Allen",
        "hashedPass000"
    );
    User savedUser = userService.createUser(user);

    savedUser.setHashedPassword("newHashedPass000");
    User updatedUser = userService.updateUser(savedUser);

    assertEquals("newHashedPass000", updatedUser.getHashedPassword());
  }
}
