package me.magicbudget.service;

import me.magicbudget.dto.outgoing_response.StatsResponse;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public final class StatsService {
  private final UserService userService;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;

  public StatsService(UserService userService, IncomeService incomeService,
      ExpenseService expenseService) {
    this.userService = userService;
    this.incomeService = incomeService;
    this.expenseService = expenseService;
  }

  public StatsResponse getStats(UUID userId) {
    return new StatsResponse();
  }
}
