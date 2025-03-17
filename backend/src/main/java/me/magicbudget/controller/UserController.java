package me.magicbudget.controller;

import me.magicbudget.dto.incomingrequest.LoginUserRequest;
import me.magicbudget.dto.outgoingresponse.LoginUserResponse;
import me.magicbudget.dto.incomingrequest.RegistrationAndAuthRequest;
import me.magicbudget.security.service.RegistrationAndAuthService;
import me.magicbudget.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

            return ResponseEntity.ok().headers(headers)
                .body(new LoginUserResponse(user.getId()));
          })
          .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
//
//  @GetMapping("/api/hello")
//  public ResponseEntity<String> helloUser() {
//    return new ResponseEntity<>("Hello from JWT", HttpStatus.OK);
//  }
//
//  @GetMapping("/{id}")
//  public ResponseEntity<User> getUserById(@PathVariable UUID id) {
//    Optional<User> user = userService.getUserById(id);
//    return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
//        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//  }
}

