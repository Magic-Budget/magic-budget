package me.magicbudget.magicbudget.service;

import me.magicbudget.magicbudget.model.User;
import me.magicbudget.magicbudget.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Test
  void testCreateUser() {
    var user = new User(null, "testuser", "bruce", "wayne", "");

    var savedUser = userService.createUser(user);

    assertNotNull(savedUser.id(), "User ID should not be null after saving");
    assertEquals("testuser", savedUser.username(), "Username should match");

    System.out.println("Does this work?");
  }

  @Test
  void testGetUserById() {
    User user = new User(
        null,
        "testuser2",
        "Clark",
        "Kent",
        "hashedPass456"
    );
    User savedUser = userService.createUser(user);

    Optional<User> retrievedUserOpt = userService.getUserById(savedUser.id());

    assertTrue(retrievedUserOpt.isPresent(), "User should be present");
    User retrievedUser = retrievedUserOpt.get();
    assertEquals(savedUser.id(), retrievedUser.id());
    assertEquals("testuser2", retrievedUser.username());
    assertEquals("Clark", retrievedUser.firstName());
  }


  @Test
  void testGetUserByUsername() {
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
    assertEquals("testuser3", retrievedUser.username());
    assertEquals("Diana", retrievedUser.firstName());
    assertEquals("Prince", retrievedUser.lastName());
    assertEquals("hashedPass789", retrievedUser.hashedPassword());
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

    assertEquals("newHashedPass000", updatedUser.hashedPassword());
  }
}
