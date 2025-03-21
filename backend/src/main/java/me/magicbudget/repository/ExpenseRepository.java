package me.magicbudget.repository;

import me.magicbudget.model.Expense;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends  JpaRepository<Expense, UUID> {
    List<Expense> findExpenseByUserId(User userId);

    Optional<Expense> findExpenseById(UUID expenseId);
}
