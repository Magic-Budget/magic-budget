package me.magicbudget.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.magicbudget.model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, UUID> {

  List<SavingGoal> findByUserId(UUID userId);
}
