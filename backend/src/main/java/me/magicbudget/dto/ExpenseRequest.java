package me.magicbudget.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenseRequest {

  private final BigDecimal amount;
  private final LocalDateTime expenseDate;
  public ExpenseRequest(BigDecimal amount, LocalDateTime expenseDate) {
    this.amount = amount;
    this.expenseDate = expenseDate;
  }
  public BigDecimal getAmount() {
    return amount;
  }

  public LocalDateTime getExpenseDate() {
    return expenseDate;
  }
}
