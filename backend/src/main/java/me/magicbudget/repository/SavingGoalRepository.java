package me.magicbudget.repository;

import me.magicbudget.model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, UUID> {

  List<SavingGoal> findByUserId(UUID userId);
}
