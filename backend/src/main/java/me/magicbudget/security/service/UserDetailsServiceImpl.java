package me.magicbudget.security.service;

import me.magicbudget.repository.UserInformationRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserInformationRepository userInformationRepository;

  public UserDetailsServiceImpl(
      UserInformationRepository userInformationRepository) {
    this.userInformationRepository = userInformationRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails user = userInformationRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User with username" + username + "not found");
    }
    return user;
  }
}
