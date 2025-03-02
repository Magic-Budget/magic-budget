package me.magicbudget.controller;

import me.magicbudget.dto.LoginUserRequest;
import me.magicbudget.dto.LoginUserResponse;
import me.magicbudget.dto.RegistrationAndAuthRequest;
import me.magicbudget.model.User;
import me.magicbudget.security.service.RegistrationAndAuthService;
import me.magicbudget.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {

  private final RegistrationAndAuthService registrationAndAuthService;
  private final UserService userService;

  public UserController(RegistrationAndAuthService registrationAndAuthService,
      UserService userService) {
    this.registrationAndAuthService = registrationAndAuthService;
    this.userService = userService;
  }

  @PostMapping("/api/auth/register")
  public ResponseEntity<String> registerUser(@RequestBody RegistrationAndAuthRequest request) {
    if (request.username() == null || request.password() == null) {
      return new ResponseEntity<>("Invalid Details", HttpStatus.UNAUTHORIZED);
    }

    if (!registrationAndAuthService.registerUser(request)) {
      return new ResponseEntity<>("Username already Taken", HttpStatus.UNAUTHORIZED);
    }

    return new ResponseEntity<>("Registration complete", HttpStatus.OK);
  }

  @PostMapping("/api/auth/sign-in")
  public ResponseEntity<LoginUserResponse> authenticateUser(@RequestBody LoginUserRequest request) {
    if (request.username() == null || request.password() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      String jwtToken = registrationAndAuthService.authenticate(request);

      return userService.getUserByUsername(request.username())
          .map(user -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
            headers.add("X-User-Id", user.getId().toString());

            return ResponseEntity.ok().headers(headers)
                .body(new LoginUserResponse(user.getId(), jwtToken, user.getFirstName(),
                    user.getLastName()));
          })
          .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/api/hello")
  public ResponseEntity<String> helloUser() {
    return new ResponseEntity<>("Hello from JWT", HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User userRequest) {
    // Fetch the existing user first
    Optional<User> existingUserOpt = userService.getUserById(id);
    if (existingUserOpt.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    User existingUser = existingUserOpt.get();

    if (userRequest.getUsername() != null) {
      existingUser.setUsername(userRequest.getUsername());
    }
    if (userRequest.getFirstName() != null) {
      existingUser.setFirstName(userRequest.getFirstName());
    }
    if (userRequest.getLastName() != null) {
      existingUser.setLastName(userRequest.getLastName());
    }
    if (userRequest.getPassword() != null) {
      existingUser.setPassword(userRequest.getPassword());
    }

    User updatedUser = userService.updateUser(existingUser);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }


  @GetMapping("/username/{username}")
  public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
    Optional<User> user = userService.getUserByUsername(username);
    return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable UUID id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

