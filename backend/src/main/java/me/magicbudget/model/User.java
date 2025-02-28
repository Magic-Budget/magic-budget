package me.magicbudget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auth_users")
public class User implements UserDetails {

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
  private String password;

  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(UUID id, String username, String firstName, String lastName, String password) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public @NotNull @Size(max = 50) String getUsername() {
    return username;
  }

  public void setUsername(@NotNull @Size(max = 50) String username) {
    this.username = username;
  }

  public @NotNull @Size(max = 50) String getFirstName() {
    return firstName;
  }

  public void setFirstName(@NotNull @Size(max = 50) String firstName) {
    this.firstName = firstName;
  }

  public @NotNull @Size(max = 50) String getLastName() {
    return lastName;
  }

  public void setLastName(@NotNull @Size(max = 50) String lastName) {
    this.lastName = lastName;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  public @NotNull String getPassword() {
    return password;
  }

  public void setPassword(@NotNull String hashedPassword) {
    this.password = hashedPassword;
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
