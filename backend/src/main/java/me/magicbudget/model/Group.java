package me.magicbudget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String groupName;

  @ManyToMany
  @JoinTable(
      name = "user_group", // name of the join table
      joinColumns = @JoinColumn(name = "group_id"), // column for the group
      inverseJoinColumns = @JoinColumn(name = "user_id") // column for the user
  )
  private List<UserInformation> members;

  @OneToMany(mappedBy = "group") // One-to-many relationship with SplitTransaction
  private List<SplitTransaction> transactions;

  public Group() {
  }

  public Group(String groupName) {
    this.groupName = groupName;
    this.members = new ArrayList<>();
    this.transactions = new ArrayList<>();
  }

  // Getters and setters
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public List<UserInformation> getMembers() {
    return members;
  }

  public void addMember(UserInformation member) {
    members.add(member);
  }

  public List<SplitTransaction> getTransactions() {
    return transactions;
  }

  public void addTransaction(SplitTransaction transaction) {
    transactions.add(transaction);
  }

}

