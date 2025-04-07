package me.magicbudget.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.Expense;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

  List<Expense> findExpenseByUserId(User userId);

  Optional<Expense> findExpenseById(UUID expenseId);
}
