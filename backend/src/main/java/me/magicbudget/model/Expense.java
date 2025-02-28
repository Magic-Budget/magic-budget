package me.magicbudget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.UUID;

@Entity
public class Expense {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User userId;

  private BigDecimal amount;

  private LocalDateTime dueDate;

  public Expense() {
  }

  public Expense(User userId, BigDecimal amount, LocalDateTime dueDate) {
    this.userId = userId;
    this.amount = amount;
    this.dueDate = dueDate;
  }

  public UUID getId() {
    return id;
  }

  public User getUserId() {
    return userId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDateTime dueDate) {
    this.dueDate = dueDate;
  }
}
