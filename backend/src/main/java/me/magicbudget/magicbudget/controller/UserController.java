package me.magicbudget.magicbudget.controller;

import me.magicbudget.magicbudget.model.User;
import me.magicbudget.magicbudget.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    User createdUser = userService.createUser(user);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable UUID id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/username/{username}")
  public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
    Optional<User> user = userService.getUserByUsername(username);
    return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User userRequest) {
    // Fetch the existing user first
    Optional<User> existingUserOpt = userService.getUserById(id);
    if (existingUserOpt.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    User existingUser = existingUserOpt.get();

    if (userRequest.username() != null) {
      existingUser.setUsername(userRequest.username());
    }
    if (userRequest.firstName() != null) {
      existingUser.setFirstName(userRequest.firstName());
    }
    if (userRequest.lastName() != null) {
      existingUser.setLastName(userRequest.lastName());
    }
    if (userRequest.hashedPassword() != null) {
      existingUser.setHashedPassword(userRequest.hashedPassword());
    }

    User updatedUser = userService.updateUser(existingUser);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

}