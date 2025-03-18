package me.magicbudget.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "receipts")
public class Receipt {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Lob
  @Column(name = "image")
  private String image;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Digits(integer = 19, fraction = 4)
  @Column(precision = 19, scale = 4, nullable = true)
  private BigDecimal amount;

  public Receipt() {
  }

  public Receipt(UUID id, String image, User user, BigDecimal amount) {
    this.id = id;
    this.image = image;
    this.user = user;
    this.amount = amount;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}