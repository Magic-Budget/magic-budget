package me.magicbudget.service;

import me.magicbudget.dto.outgoing_response.IncomeResponse;
import me.magicbudget.dto.outgoing_response.StatsResponse;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public final class StatsService {

  private final UserService userService;
  private final IncomeService incomeService;
  private final ExpenseService expenseService;
  private final GoalService goalService;

  public StatsService(UserService userService, IncomeService incomeService,
      ExpenseService expenseService, GoalService goalService) {
    this.userService = userService;
    this.incomeService = incomeService;
    this.expenseService = expenseService;
    this.goalService = goalService;
  }

  public StatsResponse getStats(UUID userId) {
    double incomes = incomeService.viewAllIncome(userId)
        .stream()
        .mapToDouble(value -> value.income_amount().doubleValue())
        .average()
        .orElse(0.0);

    double expenses = expenseService.viewExpenses(userId)
        .stream()
        .mapToDouble(value -> value.income_amount().doubleValue())
        .average()
        .orElse(0.0);

    double totalTarget = goalService.getGoalsByUserId(userId)
        .stream()
        .mapToDouble(value -> value.target().doubleValue())
        .sum();

    double totalAchieved = goalService.getGoalsByUserId(userId)
        .stream()
        .mapToDouble(value -> value.currentAmount().doubleValue())
        .sum();
    return new StatsResponse(incomes, expenses, totalTarget, totalAchieved);
  }
}
