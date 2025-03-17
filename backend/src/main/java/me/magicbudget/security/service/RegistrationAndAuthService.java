package me.magicbudget.security.service;

import me.magicbudget.dto.incomingrequest.LoginUserRequest;
import me.magicbudget.dto.incomingrequest.RegistrationAndAuthRequest;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import me.magicbudget.repository.UserInformationRepository;
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
  private final UserInformationRepository userInformationRepository;

  public RegistrationAndAuthService(PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      JwtImplementationService jwtImplementationService,
      UserInformationRepository userInformationRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtImplementationService = jwtImplementationService;
    this.userInformationRepository = userInformationRepository;
  }

  public boolean registerUser(RegistrationAndAuthRequest userRequest) {
    if (userInformationRepository.findByUsername(userRequest.username()) != null) {
      return false;
    }

    UserInformation userInformation = new UserInformation(userRequest.username(),
        passwordEncoder.encode(userRequest.password()),
        userRequest.firstName(),
        userRequest.lastName(),
        userRequest.email());

    User user = new User(userInformation);

    userInformationRepository.save(userInformation);
    user.setId(userInformation.getId());
    user.setInformation(userInformation);

    userRepository.save(user);

    return true;
  }

  public String authenticate(LoginUserRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    UserDetails user = userInformationRepository.findByUsername(request.username());
    return jwtImplementationService.generateToken(new HashMap<>(), user);
  }

}

