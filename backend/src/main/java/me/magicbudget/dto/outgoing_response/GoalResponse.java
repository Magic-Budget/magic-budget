package me.magicbudget.dto.outgoing_response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GoalResponse(UUID id, UUID userId, String name, BigDecimal target,
                           BigDecimal currentAmount, LocalDate due) {

}
