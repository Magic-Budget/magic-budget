package me.magicbudget.dto.incoming_request;

public record RegistrationAndAuthRequest(String username,
                                         String password,
                                         String firstName,
                                         String lastName,
                                         String email) {

}
