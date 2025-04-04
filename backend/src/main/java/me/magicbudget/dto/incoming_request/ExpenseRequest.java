package me.magicbudget.dto.incoming_request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import me.magicbudget.model.ExpenseCategory;

public record ExpenseRequest(BigDecimal amount,
                             LocalDateTime expenseDate,
                             String expenseName,
                             ExpenseCategory category,
                             String expenseDescription,
                             String shopName) {

}
