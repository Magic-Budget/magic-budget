package me.magicbudget.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auth_users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "information_id", referencedColumnName = "id")
  private UserInformation information;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Income> incomes;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Expense> expenses;

  public User() {
  }

  public User(UserInformation information) {
    this.information = information;
    this.incomes = new ArrayList<>();
    this.expenses = new ArrayList<>();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UserInformation getInformation() {
    return information;
  }

  public void setInformation(UserInformation information) {
    this.information = information;
  }
}
