package me.magicbudget.magicbudget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull
  @Size(max = 50)
  private String username;

  @NotNull
  @Size(max = 50)
  private String firstName;

  @NotNull
  @Size(max = 50)
  private String lastName;

  @NotNull
  private String hashedPassword;

  public User() {
  }

  public User(UUID id, String username, String firstName, String lastName, String hashedPassword) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.hashedPassword = hashedPassword;
  }

  public UUID id() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public @NotNull @Size(max = 50) String username() {
    return username;
  }

  public void setUsername(@NotNull @Size(max = 50) String username) {
    this.username = username;
  }

  public @NotNull @Size(max = 50) String firstName() {
    return firstName;
  }

  public void setFirstName(@NotNull @Size(max = 50) String firstName) {
    this.firstName = firstName;
  }

  public @NotNull @Size(max = 50) String lastName() {
    return lastName;
  }

  public void setLastName(@NotNull @Size(max = 50) String lastName) {
    this.lastName = lastName;
  }

  public @NotNull String hashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(@NotNull String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }
}
