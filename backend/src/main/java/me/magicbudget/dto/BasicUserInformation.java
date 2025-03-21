package me.magicbudget.dto;

import java.util.UUID;

public class BasicUserInformation {

  private final UUID userId;
  private final String username;
  private final String fullName;
  private final String email;

  public BasicUserInformation(UUID id, String username, String fullName, String email) {
    this.userId = id;
    this.username = username;
    this.fullName = fullName;
    this.email = email;
  }

  public UUID getUserId() { return userId; }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }
}
