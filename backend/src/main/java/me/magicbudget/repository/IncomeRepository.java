package me.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.model.Income;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, UUID> {

  List<Income> findIncomeByUserId(User user);
}
