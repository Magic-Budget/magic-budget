package me.magicbudget.dto.incoming_request;

import me.magicbudget.model.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseRequest(BigDecimal amount,
                             LocalDateTime expenseDate,
                             String expenseName,
                             ExpenseCategory category,
                             String expenseDescription,
                             String shopName) {

}
