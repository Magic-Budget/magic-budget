package me.magicbudget.service;

import me.magicbudget.dto.incomingrequest.ExpenseRequest;
import me.magicbudget.dto.outgoingresponse.ExpenseResponse;
import me.magicbudget.model.Expense;
import me.magicbudget.model.ExpenseCategory;
import me.magicbudget.model.Transaction;
import me.magicbudget.model.TransactionType;
import me.magicbudget.model.User;
import me.magicbudget.repository.ExpenseRepository;
import me.magicbudget.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

  public List<ExpenseResponse> viewExpenses(String userId) throws Exception {

    Optional<User> userById = userService.getUserById(UUID.fromString(userId));

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
    throw new RuntimeException("User not found");
  }

  @Transactional
  public void addExpense(String userId, ExpenseRequest expenseRequest) throws Exception {
    Optional<User> userById = userService.getUserById(UUID.fromString(userId));

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
            ExpenseCategory.GROCERY);
        expense.setId(transaction.getId());
        expenseRepository.save(expense);
      } catch (Exception e) {
        throw new RuntimeException("An error occurred while adding the expense", e);
      }
    } else {
      throw new RuntimeException("User not found");
    }
  }

}

