package me.magicbudget.dto;

public class BasicUserInformation {

  private final String username;
  private final String fullName;
  private final String email;

  public BasicUserInformation(String username, String fullName, String email) {
    this.username = username;
    this.fullName = fullName;
    this.email = email;
  }

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
