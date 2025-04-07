package me.magicbudget.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.Goal;
import me.magicbudget.repository.GoalRepository;
import org.springframework.stereotype.Service;

@Service
public final class GoalService {

  private final GoalRepository goalRepository;
  private final UserService userService;

  public GoalService(GoalRepository goalRepository, UserService userService) {
    this.goalRepository = goalRepository;
    this.userService = userService;
  }

  public Optional<Goal> createGoal(UUID userId, String name, BigDecimal targetAmount,
      BigDecimal currentAmount,
      LocalDate due) {
    return userService.getUserById(userId)
        .map(user -> new Goal(null, user, name, targetAmount, currentAmount, due))
        .map(goalRepository::save);
  }

  public Optional<Goal> getGoalById(UUID goalId) {
    return goalRepository.findById(goalId);
  }

  public List<Goal> getGoalsByUserId(UUID userId) {
    return goalRepository.findGoalByUser(userService.getUserById(userId).orElseThrow());
  }

  public Optional<Goal> updateGoal(UUID goalId, String name, BigDecimal targetAmount,
      BigDecimal currentAmount,
      LocalDate due) {
    return goalRepository.findById(goalId)
        .map(goal -> {
          goal.setName(name);
          goal.setTarget(targetAmount);
          goal.setCurrentAmount(currentAmount);
          goal.setDue(due);
          return goalRepository.save(goal);
        });
  }

  public void deleteGoal(UUID goalId) {
    goalRepository.deleteById(goalId);
  }
}
