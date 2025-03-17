package me.magicbudget.dto.incomingrequest;

import me.magicbudget.model.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseRequest(BigDecimal amount,
                             LocalDateTime expenseDate,
                             String expenseName,
                             ExpenseCategory expenseCategory,
                             String expenseDescription,
                             String shopName) {

}
