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
import java.util.UUID;

@Entity
@Table(name = "saving_goals",
    indexes = {@Index(name = "idx_saving_goal_user_id", columnList = "user_id")})
public class SavingGoal {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Size(max = 50)
  private String name;

  @NotNull
  @Digits(integer = 19, fraction = 4)
  @Column(precision = 19, scale = 4)
  private BigDecimal amount;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public UUID id() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public @Size(max = 50) String name() {
    return name;
  }

  public void setName(@Size(max = 50) String name) {
    this.name = name;
  }

  public @NotNull @Digits(integer = 19, fraction = 4) BigDecimal amount() {
    return amount;
  }

  public void setAmount(@NotNull @Digits(integer = 19, fraction = 4) BigDecimal amount) {
    this.amount = amount;
  }

  public User user() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
