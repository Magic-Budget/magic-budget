package me.magicbudget.dto.incoming_request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class CreditDebtCreateRequest {

    @NotNull
    private UUID businessId;
    
    @NotNull
    private UUID userId;
    
    @NotNull
    @Digits(integer = 19, fraction = 4)
    private BigDecimal amount;
    
    @NotNull
    @Digits(integer = 19, fraction = 4)
    private BigDecimal interestRate;
    
    public CreditDebtCreateRequest(UUID businessId, UUID userId, BigDecimal amount, BigDecimal interestRate) {
        this.businessId = businessId;
        this.userId = userId;
        this.amount = amount;
        this.interestRate = interestRate;
    }
    
    // Getters and setters
    public UUID getBusinessId() {
        return businessId;
    }
    
    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}