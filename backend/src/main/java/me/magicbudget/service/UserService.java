package me.magicbudget.service;

import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import me.magicbudget.repository.UserInformationRepository;
import me.magicbudget.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public final class UserService {

  private final UserRepository userRepository;
  private final UserInformationRepository userInformationRepository;

  public UserService(UserRepository userRepository,
      UserInformationRepository userInformationRepository) {
    this.userRepository = userRepository;
    this.userInformationRepository = userInformationRepository;
  }

  public User createUser(@NonNull User user) {
    return userRepository.save(user);
  }

  public Optional<User> getUserById(@NonNull UUID userId) {
    return userRepository.findById(userId);
  }

  public Optional<UserInformation> getUserByUsername(@NonNull String username) {
    return Optional.ofNullable(userInformationRepository.findByUsername(username));
  }


  public User updateUser(@NonNull User user) {
    return userRepository.save(user);
  }
}
