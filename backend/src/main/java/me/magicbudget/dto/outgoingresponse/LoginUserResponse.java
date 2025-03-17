package me.magicbudget.dto.outgoingresponse;

import java.util.UUID;

public record LoginUserResponse(UUID userId, String token, String firstName, String lastName) {

}
