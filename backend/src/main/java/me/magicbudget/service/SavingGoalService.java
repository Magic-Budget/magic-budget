package me.magicbudget.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.SavingGoal;
import me.magicbudget.model.User;
import me.magicbudget.repository.SavingGoalRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public final class SavingGoalService {

  private final SavingGoalRepository savingGoalRepository;
  private final UserService userService;

  public SavingGoalService(SavingGoalRepository savingGoalRepository, UserService userService) {
    this.savingGoalRepository = savingGoalRepository;
    this.userService = userService;
  }

  public SavingGoal createSavingGoal(@NonNull SavingGoal savingGoal, @NonNull UUID userId) {
    User user = userService.getUserById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

    savingGoal.setUser(user);
    return savingGoalRepository.save(savingGoal);
  }

  public Optional<SavingGoal> getSavingGoalById(@NonNull UUID id) {
    return savingGoalRepository.findById(id);
  }

  public List<SavingGoal> getSavingGoalsByUserId(@NonNull UUID userId) {
    return savingGoalRepository.findByUserId(userId);
  }

  public SavingGoal updateSavingGoal(@NonNull SavingGoal savingGoal) {
    return savingGoalRepository.save(savingGoal);
  }

  public void deleteSavingGoal(@NonNull UUID id) {
    savingGoalRepository.deleteById(id);
  }

  public void deleteAllSavingGoalsByUserId(@NonNull UUID userId) {
    List<SavingGoal> userGoals = savingGoalRepository.findByUserId(userId);
    savingGoalRepository.deleteAll(userGoals);
  }
}
