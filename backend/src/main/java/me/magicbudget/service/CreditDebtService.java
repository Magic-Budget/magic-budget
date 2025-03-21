package me.magicbudget.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.CreditDebt;
import me.magicbudget.model.User;
import me.magicbudget.repository.CreditDebtRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public final class CreditDebtService {

  private final CreditDebtRepository creditDebtRepository;
  private final UserService userService;

  public CreditDebtService(CreditDebtRepository creditDebtRepository, UserService userService) {
    this.creditDebtRepository = creditDebtRepository;
    this.userService = userService;
  }

  public CreditDebt createCreditDebt(@NonNull CreditDebt creditDebt, @NonNull UUID userId) {
    User user = userService.getUserById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

    creditDebt.setUser(user);
    return creditDebtRepository.save(creditDebt);
  }

  public Optional<CreditDebt> getCreditDebtById(@NonNull UUID id) {
    return creditDebtRepository.findById(id);
  }

  public List<CreditDebt> getCreditDebtsByUserId(@NonNull UUID userId) {
    return creditDebtRepository.findByUserId(userId);
  }

  public CreditDebt updateCreditDebt(@NonNull CreditDebt creditDebt) {
    return creditDebtRepository.save(creditDebt);
  }

  public void deleteCreditDebt(@NonNull UUID id) {
    creditDebtRepository.deleteById(id);
  }

  public void deleteAllCreditDebtsByUserId(@NonNull UUID userId) {
    List<CreditDebt> userDebts = creditDebtRepository.findByUserId(userId);
    creditDebtRepository.deleteAll(userDebts);
  }


}