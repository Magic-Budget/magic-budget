package me.magicbudget.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.dto.incoming_request.ExpenseRequest;
import me.magicbudget.dto.outgoing_response.ExpenseResponse;
import me.magicbudget.model.Expense;
import me.magicbudget.model.Transaction;
import me.magicbudget.model.TransactionType;
import me.magicbudget.model.User;
import me.magicbudget.repository.ExpenseRepository;
import me.magicbudget.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public void addExpense(UUID userId, ExpenseRequest expenseRequest)
      throws IllegalArgumentException {
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
            expenseRequest.category());
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

  public ExpenseResponse viewExpense(UUID userId, UUID expenseId) {
    Optional<User> userById = userService.getUserById(userId);
    if (userById.isPresent()) {
      Optional<Expense> expense = expenseRepository.findExpenseById(expenseId);
      if (expense.isPresent()) {
        Transaction transaction = expense.get().getTransaction();
        return new ExpenseResponse(expense.get().getId(),
            transaction.getAmount(),
            transaction.getName(),
            transaction.getDescription(),
            transaction.getTrasnactionDate(),
            expense.get().getExpenseCategory(),
            expense.get().getShopName());
      }
    }
    throw new IllegalArgumentException("User or Expense not found");
  }

  @Transactional(rollbackFor = IllegalArgumentException.class)
  public void updateExpense(UUID userID, UUID expenseID, ExpenseRequest updatedExpense) {
    Optional<User> userByID = userService.getUserById(userID);

    if (userByID.isPresent()) {
      try {
        Transaction existingTransaction = transactionRepository.findById(expenseID).orElseThrow(
            () -> new IllegalArgumentException("Could not find transaction to update."));

        existingTransaction.setName(updatedExpense.expenseName());
        existingTransaction.setAmount(updatedExpense.amount());
        existingTransaction.setTransactionDate(updatedExpense.expenseDate());
        existingTransaction.setDescription(updatedExpense.expenseDescription());
        transactionRepository.save(existingTransaction);
      } catch (Exception e) {
        throw new IllegalArgumentException("An error occurred while updating the expense", e);
      }
    } else {
      throw new IllegalArgumentException("User not found");
    }
  }

  public Boolean splitExpense(UUID userID, UUID expenseID, List<UUID> splitWith) {
    try {
      ExpenseResponse expenseResponse = this.viewExpense(userID, expenseID);
      BigDecimal amount = expenseResponse.income_amount();
      BigDecimal individual_amount = amount.divide(BigDecimal.valueOf(splitWith.size() + 1));

      ExpenseRequest updatedExpense = new ExpenseRequest(
          individual_amount,
          expenseResponse.expense_posted_date(),
          expenseResponse.expense_name(),
          expenseResponse.category(),
          expenseResponse.expense_description(),
          expenseResponse.business_name()
      );

      this.updateExpense(userID, expenseID, updatedExpense);

      splitWith.forEach((id) -> {
        this.addExpense(id, updatedExpense);
      });
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}

