package me.magicbudget.dto.incomingrequest;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class SavingGoalCreateRequest {
    
    @Size(max = 50)
    private String name;
    
    @NotNull
    @Digits(integer = 19, fraction = 4)
    private BigDecimal amount;
    
    public SavingGoalCreateRequest(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}