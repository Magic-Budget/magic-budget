package me.magicbudget.magicbudget.service;

import me.magicbudget.magicbudget.model.User;
import me.magicbudget.magicbudget.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public final class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(@NonNull User user) {
    return userRepository.save(user);
  }

  public Optional<User> getUserById(@NonNull UUID userId) {
    return userRepository.findById(userId);
  }

  public Optional<User> getUserByUsername(@NonNull String username) {
    return userRepository.findByUsername(username);
  }

  public User updateUser(@NonNull User user) {
    return userRepository.save(user);
  }
}
