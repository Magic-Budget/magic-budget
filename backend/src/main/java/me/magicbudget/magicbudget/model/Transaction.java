package me.magicbudget.magicbudget.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions",
    indexes = {@Index(name = "idx_transactions_user_id", columnList = "user_id")})
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Size(max = 50)
  private String name;

  @NotNull
  @Column(name = "transaction_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime transactionDate;

  @NotNull
  @Digits(integer = 19, fraction = 4)
  @Column(precision = 19, scale = 4)
  private BigDecimal amount;

  @NotNull
  private String description;

  public Transaction() {
  }

  public Transaction(UUID id, User user, String name, LocalDateTime transactionDate,
      BigDecimal amount, String description) {
    this.id = id;
    this.user = user;
    this.name = name;
    this.transactionDate = transactionDate;
    this.amount = amount;
    this.description = description;
  }

  public UUID id() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User user() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public @Size(max = 50) String name() {
    return name;
  }

  public void setName(@Size(max = 50) String name) {
    this.name = name;
  }

  public @NotNull LocalDateTime transactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(@NotNull LocalDateTime transactionDate) {
    this.transactionDate = transactionDate;
  }

  public @NotNull @Digits(integer = 19, fraction = 4) BigDecimal amount() {
    return amount;
  }

  public void setAmount(@NotNull @Digits(integer = 19, fraction = 4) BigDecimal amount) {
    this.amount = amount;
  }

  public @NotNull String description() {
    return description;
  }

  public void setDescription(@NotNull String description) {
    this.description = description;
  }

}
