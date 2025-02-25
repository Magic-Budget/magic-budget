package me.magicbudget.data.entities;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="auth_users")
public class AuthUsers implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String username;

  private String password;

  @Enumerated(value = EnumType.STRING)
  private Role role;


  public AuthUsers(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }
  public AuthUsers() {

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
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(this.role.name()));
  }
  @Override
  public String getPassword() {
    return this.password;
  }
  @Override
  public String getUsername() {
    return this.username;
  }

  public UUID getId() {
    return id;
  }
}

