package me.magicbudget.security.service;

import me.magicbudget.dto.LoginUserRequest;
import me.magicbudget.dto.RegistrationAndAuthRequest;
import me.magicbudget.model.User;
import me.magicbudget.security.jwt.JwtImplementationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import me.magicbudget.repository.UserRepository;


import java.util.HashMap;

@Service
public class RegistrationAndAuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtImplementationService jwtImplementationService;

  public RegistrationAndAuthService(PasswordEncoder passwordEncoder, UserRepository userRepository,
      AuthenticationManager authenticationManager,
      JwtImplementationService jwtImplementationService) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtImplementationService = jwtImplementationService;
  }

  public boolean registerUser(RegistrationAndAuthRequest userRequest) {
    if (userRepository.findUserByUsername(userRequest.username()) != null) {
      return false;
    }
    User user = new User(null, userRequest.username(),
        userRequest.firstName(), userRequest.lastName(),
        passwordEncoder.encode(userRequest.password()));
    userRepository.save(user);
    return true;
  }

  public String authenticate(LoginUserRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    UserDetails user = userRepository.findUserByUsername(request.username());
    return jwtImplementationService.generateToken(new HashMap<>(), user);
  }

}

