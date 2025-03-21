package me.magicbudget.dto.outgoing_response;

import java.util.UUID;

public record LoginUserResponse(UUID userId, String token, String firstName, String lastName) {

}
