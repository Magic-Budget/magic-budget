package me.magicbudget.controller;

import me.magicbudget.dto.incoming_request.LoginUserRequest;
import me.magicbudget.dto.outgoing_response.LoginUserResponse;
import me.magicbudget.dto.incoming_request.RegistrationAndAuthRequest;
import me.magicbudget.model.UserInformation;
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
  public ResponseEntity<LoginUserResponse> registerUser(
      @RequestBody RegistrationAndAuthRequest request) {
    if (request.username() == null || request.password() == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    if (!registrationAndAuthService.registerUser(request)) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    String jwtToken = registrationAndAuthService.authenticate(
        new LoginUserRequest(request.email(), request.password()));
    UserInformation userInformation = userService.getUserByUsername(request.username()).get();

    return new ResponseEntity<>(
        new LoginUserResponse(userInformation.getId(), jwtToken, request.firstName(),
            request.lastName()), HttpStatus.CREATED);
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
                .body(new LoginUserResponse(user.getId(), jwtToken, user.getFirstName(),
                    user.getLastName()));
          })
          .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}

