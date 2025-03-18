package me.magicbudget.model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Table(name = "receipts")
public class Receipt {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Lob
  @Column(name = "data")
  private byte[] data;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotNull
  @Digits(integer = 19, fraction = 4)
  @Column(precision = 19, scale = 4)
  private BigDecimal amount;

  public Receipt() {
  }

  public Receipt(UUID id, byte[] data, User user, BigDecimal amount) {
    this.id = id;
    this.data = data;
    this.user = user;
    this.amount = amount;
  }

  public UUID id() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public byte[] data() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public User user() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public BigDecimal amount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "Receipt{" +
        "id=" + id +
        ", data=" + Arrays.toString(data) +
        ", user=" + user +
        ", amount=" + amount +
        '}';
  }
}
