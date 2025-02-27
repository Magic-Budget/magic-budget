package me.magicbudget.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for creating a new transaction.
 */
public class TransactionCreateRequest {

    @Size(max = 50)
    private String name;

    @NotNull
    private LocalDateTime transactionDate;

    @NotNull
    @Digits(integer = 19, fraction = 4)
    private BigDecimal amount;

    @NotNull
    private String description;

    public TransactionCreateRequest(String name, LocalDateTime transactionDate,
            BigDecimal amount, String description) {
        this.name = name;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
