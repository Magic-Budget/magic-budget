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

/**
 * {
 *   "userId" : "dsfdsf"
 *   "amount" :35.6
 *   "date" : "Date.now()"
 * }
 */

@Entity
public class Income {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  private User userId;

  private BigDecimal amount;

  private LocalDateTime date;

  public Income(User userId, BigDecimal amount, LocalDateTime date) {
    this.userId = userId;
    this.amount = amount;
    this.date = date;
  }

  public Income() {
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

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }
}
