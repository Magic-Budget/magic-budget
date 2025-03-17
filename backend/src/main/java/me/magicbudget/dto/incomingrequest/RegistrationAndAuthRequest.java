package me.magicbudget.dto.incomingrequest;

public record RegistrationAndAuthRequest(String username,
                                         String password,
                                         String firstName,
                                         String lastName,
                                         String email) {

}
