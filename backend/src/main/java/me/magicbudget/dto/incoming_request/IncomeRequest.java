package me.magicbudget.dto.incoming_request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import me.magicbudget.model.IncomeType;

public record IncomeRequest(String name,
                            String description,
                            BigDecimal amount,
                            IncomeType type,
                            LocalDateTime date) {

}
