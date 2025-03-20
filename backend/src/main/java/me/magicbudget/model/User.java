package me.magicbudget.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auth_users")
public class User{
  
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

  @Id
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setInformation(UserInformation information) {
    this.information = information;
  }

  public UserInformation getInformation() {
    return information;
  }
}
