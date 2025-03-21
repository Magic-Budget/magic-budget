package me.magicbudget.service;

import me.magicbudget.dto.incoming_request.IncomeRequest;
import me.magicbudget.dto.outgoing_response.IncomeResponse;
import me.magicbudget.model.Income;
import me.magicbudget.model.Transaction;
import me.magicbudget.model.TransactionType;
import me.magicbudget.model.User;
import me.magicbudget.repository.IncomeRepository;
import me.magicbudget.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


  public List<IncomeResponse> viewAllIncome(String userId) throws Exception {
    Optional<User> userById = userService.getUserById(UUID.fromString(userId));

    if (userById.isPresent()) {
      List<Income> incomes = incomeRepository.findIncomeByUserId(userById.get());

      return incomes.stream().map(income -> {

        Transaction transaction = income.getTransaction();

        return new IncomeResponse(transaction.getId(),
            transaction.getAmount(),
            transaction.getName(),
            transaction.getDescription(),
            transaction.getTrasnactionDate(),
            income.getType()
        );
      }).toList();
    }
    throw new RuntimeException("User not found");
  }

  @Transactional
  public void addIncome(String userId,IncomeRequest incomeRequest) throws Exception {
    Optional<User> userById = userService.getUserById(UUID.fromString(userId));
    if (userById.isPresent()) {
      User user = userById.get();
      try {
        Transaction transaction = new Transaction(incomeRequest.name(),
            incomeRequest.date(),
            incomeRequest.amount(),
            incomeRequest.description(),
            TransactionType.INCOME);

        transactionRepository.save(transaction);

        Income income = new Income(user, transaction, incomeRequest.type());
        income.setId(transaction.getId());
        incomeRepository.save(income);
      }
      catch (Exception e) {
        throw new RuntimeException("An error occurred while adding the income", e);
      }

    }
    throw new RuntimeException("User not found");
  }

}
