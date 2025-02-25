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
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "credit_debts",
    indexes = {@Index(name = "idx_cred_debt_user_id", columnList = "user_id"),
        @Index(name = "idx_credit_debt_business_id", columnList = "business_id")})
public class CreditDebt {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "business_id", nullable = false)
  private Business business;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotNull
  @Digits(integer = 19, fraction = 4)
  @Column(precision = 19, scale = 4)
  private BigDecimal amount;

  @NotNull
  @Digits(integer = 19, fraction = 4)
  @Column(precision = 19, scale = 4)
  private BigDecimal interestRate;

  public CreditDebt() {
  }

  public CreditDebt(UUID id, Business business, User user, BigDecimal amount,
      BigDecimal interestRate) {
    this.id = id;
    this.business = business;
    this.user = user;
    this.amount = amount;
    this.interestRate = interestRate;
  }

  public UUID id() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Business business() {
    return business;
  }

  public void setBusiness(Business business) {
    this.business = business;
  }

  public User user() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public @NotNull @Digits(integer = 19, fraction = 4) BigDecimal amount() {
    return amount;
  }

  public void setAmount(@NotNull @Digits(integer = 19, fraction = 4) BigDecimal amount) {
    this.amount = amount;
  }

  public @NotNull @Digits(integer = 19, fraction = 4) BigDecimal interestRate() {
    return interestRate;
  }

  public void setInterestRate(
      @NotNull @Digits(integer = 19, fraction = 4) BigDecimal interestRate) {
    this.interestRate = interestRate;
  }
}
