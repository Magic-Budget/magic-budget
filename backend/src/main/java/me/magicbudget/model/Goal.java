package me.magicbudget.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "goals")
public class Goal {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Size(max = 50)
  private String name;

  @NotNull
  @Digits(integer = 19, fraction = 4)
  @Column(precision = 19, scale = 4)
  private BigDecimal target;

  @NotNull
  @Digits(integer = 19, fraction = 4)
  @Column(precision = 19, scale = 4)
  private BigDecimal currentAmount;

  @NotNull
  private LocalDate due;

  public Goal() {
  }

  public Goal(UUID id, User user, String name, BigDecimal target, BigDecimal currentAmount,
      LocalDate due) {
    this.id = id;
    this.user = user;
    this.name = name;
    this.target = target;
    this.currentAmount = currentAmount;
    this.due = due;
  }

  public UUID id() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User user() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String name() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal target() {
    return target;
  }

  public void setTarget(BigDecimal target) {
    this.target = target;
  }

  public BigDecimal currentAmount() {
    return currentAmount;
  }

  public void setCurrentAmount(BigDecimal currentAmount) {
    this.currentAmount = currentAmount;
  }

  public LocalDate due() {
    return due;
  }

  public void setDue(LocalDate due) {
    this.due = due;
  }
}
