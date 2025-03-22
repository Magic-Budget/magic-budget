package me.magicbudget.dto.incoming_request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRequest {

  private final String name;
  private final LocalDateTime transactionDate;
  private final BigDecimal amount;
  private final String description;

  private final List<String> users;

  private final String groupName;

  public TransactionRequest(String name, LocalDateTime transactionDate, BigDecimal amount,
                            String description, String groupName) {
    this.name = name;
    this.transactionDate = transactionDate;
    this.amount = amount;
    this.description = description;
    this.groupName = groupName;
    this.users = new ArrayList<>();
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

  public String getGroupName() {
    return groupName;
  }
}
