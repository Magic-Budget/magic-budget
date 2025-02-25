package me.magicbudget.controllers;

import me.magicbudget.data.dto.RegistrationAndAuthRequest;
import me.magicbudget.security.service.RegistrationAndAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  private final RegistrationAndAuthService registrationAndAuthService;

  public Controller(RegistrationAndAuthService registrationAndAuthService) {
    this.registrationAndAuthService = registrationAndAuthService;

  }

  @PostMapping("/api/v1/auth/register")
  public ResponseEntity<String> registerUser(@RequestBody RegistrationAndAuthRequest request){
    if(request.username() == null || request.password() == null)
      return new ResponseEntity<>("Invalid Details", HttpStatus.UNAUTHORIZED);

    if(!registrationAndAuthService.registerUser(request))
      return new ResponseEntity<>("Username already Taken",HttpStatus.UNAUTHORIZED);

    return new ResponseEntity<>("Registration complete",HttpStatus.OK);
  }

  @PostMapping("/api/v1/auth/sign-in")
  public ResponseEntity<String> authenticateUser(@RequestBody RegistrationAndAuthRequest request){
    if(request.username() == null || request.password() == null)
      return new ResponseEntity<>("Invalid Details",HttpStatus.UNAUTHORIZED);

    String authenticate = registrationAndAuthService.authenticate(request);
    String authorizationKey = "Bearer " + authenticate;
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION,authorizationKey);

    return new ResponseEntity<>("Your JWT token is : " + authenticate,
        headers,
        HttpStatusCode.valueOf(200));
  }

  @GetMapping("/api/v1/hello")
  public ResponseEntity<String> helloUser(){
    return new ResponseEntity<>("Hello from JWT",HttpStatus.OK);
  }
}

