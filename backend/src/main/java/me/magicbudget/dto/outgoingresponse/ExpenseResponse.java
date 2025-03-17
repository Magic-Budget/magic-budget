package me.magicbudget.dto.outgoingresponse;

import me.magicbudget.model.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
