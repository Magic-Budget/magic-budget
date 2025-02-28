package me.magicbudget.repository;

import me.magicbudget.model.Expense;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends  JpaRepository<Expense, UUID> {
    List<Expense> findExpenseByUserId(User userId);
}
