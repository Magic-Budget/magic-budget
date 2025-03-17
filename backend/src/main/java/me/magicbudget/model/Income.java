package me.magicbudget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.UUID;

@Entity
public class Income {

  @Id
  private UUID id;

  @OneToOne
  @JoinColumn(name = "transactions_id", referencedColumnName = "id")
  private Transaction transaction;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  private User userId;

  @Enumerated(EnumType.STRING)
  private IncomeType type;


  public Income(User userId,Transaction transaction, IncomeType incomeType) {
    this.userId = userId;
    this.transaction = transaction;
    this.type = incomeType;
  }

  public Income() {
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User getUserId() {
    return userId;
  }

  public UUID getId() {
    return id;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public User getUser() {
    return userId;
  }

  public IncomeType getType() {
    return type;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public void setUser(User user) {
    this.userId = user;
  }

  public void setType(IncomeType type) {
    this.type = type;
  }

}
