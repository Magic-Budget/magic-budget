package me.magicbudget.service;

import me.magicbudget.dto.incoming_request.ExpenseRequest;
import me.magicbudget.dto.outgoing_response.ExpenseResponse;
import me.magicbudget.model.Expense;
import me.magicbudget.model.ExpenseCategory;
import me.magicbudget.model.Transaction;
import me.magicbudget.model.TransactionType;
import me.magicbudget.model.User;
import me.magicbudget.repository.ExpenseRepository;
import me.magicbudget.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ExpenseService {

  private final ExpenseRepository expenseRepository;
  private final TransactionRepository transactionRepository;
  private final UserService userService;

  public ExpenseService(ExpenseRepository expenseRepository,
      TransactionRepository transactionRepository, UserService userService) {
    this.expenseRepository = expenseRepository;
    this.transactionRepository = transactionRepository;
    this.userService = userService;
  }

  public List<ExpenseResponse> viewExpenses(UUID userId) throws IllegalArgumentException {

    Optional<User> userById = userService.getUserById(userId);

    if (userById.isPresent()) {
      List<Expense> expenses = expenseRepository.findExpenseByUserId(userById.get());
      return expenses.stream().map(expense -> {
        Transaction transaction = expense.getTransaction();
        return new ExpenseResponse(expense.getId(),
            transaction.getAmount(),
            transaction.getName(),
            transaction.getDescription(),
            transaction.getTrasnactionDate(),
            expense.getExpenseCategory(),
            expense.getShopName());
      }).toList();

    }
    throw new IllegalArgumentException("User not found");
  }

  @Transactional
  public void addExpense(UUID userId, ExpenseRequest expenseRequest) throws IllegalArgumentException {
    Optional<User> userById = userService.getUserById(userId);

    if (userById.isPresent()) {
      User user = userById.get();
      try {
        Transaction transaction = new Transaction(
            expenseRequest.expenseName(),
            expenseRequest.expenseDate(),
            expenseRequest.amount(),
            expenseRequest.expenseDescription(),
            TransactionType.EXPENSE
        );
        transactionRepository.save(transaction);
        Expense expense = new Expense(user, transaction, expenseRequest.shopName(),
            expenseRequest.expenseCategory());
        expense.setId(transaction.getId());
        expenseRepository.save(expense);
      } catch (Exception e) {
        throw new IllegalArgumentException("An error occurred while adding the expense", e);
      }
    } else {
      throw new IllegalArgumentException("User not found");
    }
  }

  public BigDecimal getTotalExpense(UUID userId) throws IllegalArgumentException {
    Optional<User> userById = userService.getUserById(userId);
    if (userById.isPresent()) {
      List<Expense> expenses = expenseRepository.findExpenseByUserId(userById.get());


      return expenses.stream()
          .map(expense -> expense.getTransaction().getAmount())
          .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    throw new IllegalArgumentException("User not found");
  }
}

