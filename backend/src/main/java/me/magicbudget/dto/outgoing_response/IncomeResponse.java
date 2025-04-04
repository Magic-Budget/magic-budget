package me.magicbudget.dto.outgoing_response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import me.magicbudget.model.IncomeType;

public record IncomeResponse(
    UUID transaction_id,
    BigDecimal income_amount,
    String income_name,
    String income_description,
    LocalDateTime income_posted_date,
    IncomeType income_type) {

}
