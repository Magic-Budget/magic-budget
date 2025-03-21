package me.magicbudget.repository;

import me.magicbudget.model.Goal;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID> {
  List<Goal> findGoalByUser(User user);
}
