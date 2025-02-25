package com.magicbudget.configuration;

import com.magicbudget.data.entities.Role;
import com.magicbudget.data.entities.User;
import com.magicbudget.data.repository.UserRepository;
import com.magicbudget.security.service.UserDetailsServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfiguration   {

  private final UserDetailsServiceImpl userDetailsService ;

  public ApplicationConfiguration(UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authentication) throws Exception {
    return authentication.getAuthenticationManager();
  }
  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
  @Bean
  public AuthenticationProvider authenticationProvider(){

    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    authenticationProvider.setUserDetailsService(userDetailsService);
    return authenticationProvider;
  }
  @Bean
  public CommandLineRunner commandLineRunner(UserRepository userRepository){
    return args -> {
      User user = new User("AdminUser",passwordEncoder().encode("1234"), Role.USER);
      userRepository.save(user);
    };
  }

}

