package me.magicbudget.dto;

public record RegistrationAndAuthRequest(String username,
                                         String password,
                                         String firstName,
                                         String lastName) {

}
