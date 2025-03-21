package me.magicbudget.dto.incoming_request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalCreateRequest(String name, BigDecimal target, BigDecimal currentAmount,
                                LocalDate due) {

}
