package me.magicbudget.security.service;

import me.magicbudget.data.dto.RegistrationAndAuthRequest;
import me.magicbudget.data.entities.AuthUsers;
import me.magicbudget.data.entities.Role;
import me.magicbudget.data.repository.UserRepository;
import me.magicbudget.security.jwt.JwtImplementationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  public boolean registerUser(RegistrationAndAuthRequest userRequest){
    if(userRepository.findUserByUsername(userRequest.username()) != null){
      return false;
    }
    AuthUsers authUsers = new AuthUsers(userRequest.username(), passwordEncoder.encode(userRequest.password()), Role.USER);
    userRepository.save(authUsers);
    return true;
  }

  public String authenticate(RegistrationAndAuthRequest userRequest){
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.username(),userRequest.password()));
    UserDetails user = userRepository.findUserByUsername(userRequest.username());
    return jwtImplementationService.generateToken(new HashMap<>(),user);
  }

}

