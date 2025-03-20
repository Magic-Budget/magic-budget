package me.magicbudget.service;

import me.magicbudget.dto.incomingrequest.IncomeRequest;
import me.magicbudget.dto.outgoingresponse.IncomeResponse;
import me.magicbudget.model.Income;
import me.magicbudget.model.IncomeType;
import me.magicbudget.model.Transaction;
import me.magicbudget.model.TransactionType;
import me.magicbudget.model.User;
import me.magicbudget.repository.IncomeRepository;
import me.magicbudget.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static me.magicbudget.model.IncomeType.BIWEEKLY;
import static me.magicbudget.model.IncomeType.MONTHLY;
import static me.magicbudget.model.IncomeType.ONETIME;
import static me.magicbudget.model.IncomeType.WEEKLY;
import static me.magicbudget.model.IncomeType.YEARLY;

@Service
public class IncomeService {

  private final IncomeRepository incomeRepository;
  private final TransactionRepository transactionRepository;
  private final UserService userService;

  public IncomeService(IncomeRepository incomeRepository,
      TransactionRepository transactionRepository, UserService userService) {
    this.incomeRepository = incomeRepository;
    this.transactionRepository = transactionRepository;
    this.userService = userService;
  }


  public List<IncomeResponse> viewAllIncome(UUID userId) throws IllegalArgumentException {
    Optional<User> userById = userService.getUserById(userId);

    if (userById.isPresent()) {
      List<Income> incomes = incomeRepository.findIncomeByUserId(userById.get());

      return incomes.stream().map(income -> {

        Transaction transaction = income.getTransaction();

        return new IncomeResponse(transaction.getId(), transaction.getAmount(),
            transaction.getName(), transaction.getDescription(), transaction.getTrasnactionDate(),
            income.getType());
      }).toList();
    }
    throw new IllegalArgumentException("User not found");
  }

  @Transactional
  public void addIncome(UUID userId, IncomeRequest incomeRequest) throws IllegalArgumentException {
    Optional<User> userById = userService.getUserById(userId);
    if (userById.isPresent()) {
      User user = userById.get();
      try {
        Transaction transaction = new Transaction(incomeRequest.name(), incomeRequest.date(),
            incomeRequest.amount(), incomeRequest.description(), TransactionType.INCOME);

        transactionRepository.save(transaction);

        Income income = new Income(user, transaction, incomeRequest.type());
        income.setId(transaction.getId());
        incomeRepository.save(income);
      } catch (Exception e) {
        throw new IllegalArgumentException("An error occurred while adding the income", e);
      }

    }
    throw new IllegalArgumentException("User not found");
  }

  public BigDecimal totalIncome(UUID userId) throws IllegalArgumentException {

    Optional<User> userById = userService.getUserById(userId);
    if (userById.isPresent()) {

      List<Income> incomeList = incomeRepository.findIncomeByUserId(userById.get());

      BigDecimal totalIncome = BigDecimal.ZERO;
      LocalDateTime today = LocalDateTime.now();

      for (Income income : incomeList) {
        Transaction transaction = income.getTransaction();
        IncomeType type = income.getType();

        if (transaction != null && type != null) {
          // Calculate the number of days since the transaction date
          long daysSinceTransaction = ChronoUnit.DAYS.between(transaction.getTransactionDate(),
              today);

          int periods = switch (type) {
            case ONETIME -> 1; // Onetime is only counted once
            case YEARLY -> (int) (daysSinceTransaction / 365); // Count every year
            case MONTHLY -> (int) (daysSinceTransaction / 30); // Count every month
            case BIWEEKLY -> (int) (daysSinceTransaction / 14); // Count every two weeks
            case WEEKLY -> (int) (daysSinceTransaction / 7); // Count every week
          };

          // Calculate how many periods should be counted based on the IncomeType

          // If periods are greater than 0, calculate the income
          if (periods > 0) {
            BigDecimal incomeAmount = transaction.getAmount().multiply(BigDecimal.valueOf(periods));
            totalIncome = totalIncome.add(incomeAmount);
          }
        }
      }
      return totalIncome;
    }
    throw new IllegalArgumentException("User not found");
  }
}
