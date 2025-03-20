package me.magicbudget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "split_transaction")
public class SplitTransaction extends Transaction {

  @ManyToMany
  @JoinTable(name = "split_transaction_users", // Join table name
      joinColumns = @JoinColumn(name = "split_transaction_id"), inverseJoinColumns = @JoinColumn(name = "user_information_id"))
  private List<UserInformation> owedTo;

  @OneToOne
  @JoinColumn(name = "paid_by_id", referencedColumnName = "id")
  private UserInformation paidBy;

  @ManyToOne
  @JoinColumn(name = "group_id") // Foreign key for the group in this transaction
  private Group group;

  public SplitTransaction() {
    owedTo = new ArrayList<>();
  }

  public SplitTransaction(String name, LocalDateTime transactionDate,
      BigDecimal amount,
      String description, TransactionType transactionType,
      UserInformation paidBy
  ) {
    super(name, transactionDate, amount, description, transactionType);
    this.paidBy = paidBy;
    this.owedTo = new ArrayList<>(); // Ensure owedTo is initialized
  }

  public void addUser(UserInformation user) {
    owedTo.add(user);
  }

  public List<UserInformation> getOwedTo() {
    return owedTo;
  }

  public UserInformation getPaidBy() {
    return paidBy;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }
}
