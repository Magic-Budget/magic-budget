package me.magicbudget.dto.outgoing_response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import me.magicbudget.model.ExpenseCategory;

public record ExpenseResponse(
    UUID transaction_id,
    BigDecimal income_amount,
    String expense_name,
    String expense_description,
    LocalDateTime expense_posted_date,
    ExpenseCategory category,
    String business_name
) {

}
