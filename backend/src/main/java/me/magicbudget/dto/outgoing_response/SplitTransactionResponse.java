package me.magicbudget.dto.outgoing_response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SplitTransactionResponse {

  private final UUID transactionId;
  private final String name;
  private final LocalDateTime transactionDate;
  private final BigDecimal amount;
  private final String description;

  private final List<String> users;

  private final String paidBy;

  public SplitTransactionResponse(UUID transactionId, String name, LocalDateTime transactionDate, BigDecimal amount,
      String description, String paidBy) {
    this.transactionId = transactionId;
    this.name = name;
    this.transactionDate = transactionDate;
    this.amount = amount;
    this.description = description;
    this.paidBy = paidBy;
    this.users = new ArrayList<>();
  }

  public UUID getTransactionId() {
    return transactionId;
  }

  public String getPaidBy() {
    return paidBy;
  }

  public String getName() {
    return name;
  }

  public LocalDateTime getTransactionDate() {
    return transactionDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getUsers() {
    return users;
  }
}
