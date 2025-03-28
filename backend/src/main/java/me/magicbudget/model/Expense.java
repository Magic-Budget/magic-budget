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
public class Expense {

  @Id
  private UUID id;

  @OneToOne
  @JoinColumn(name = "transactions_id", referencedColumnName = "id")
  private Transaction transaction;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User userId;

  private String shopName;

  @Enumerated(EnumType.STRING)
  private ExpenseCategory expenseCategory;

  public Expense() {
  }

  public Expense(User userId,
      Transaction transaction,
      String shopName,
      ExpenseCategory expenseCategory) {
    this.userId = userId;
    this.transaction = transaction;
    this.shopName = shopName;
    this.expenseCategory = expenseCategory;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User getUserId() {
    return userId;
  }

  public String getShopName() {
    return shopName;
  }

  public ExpenseCategory getExpenseCategory() {
    return expenseCategory;
  }
}
