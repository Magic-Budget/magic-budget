package me.magicbudget.dto.incoming_request;

import me.magicbudget.model.IncomeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IncomeRequest(String name,
                            String description,
                            BigDecimal amount,
                            IncomeType type,
                            LocalDateTime date) {

}
