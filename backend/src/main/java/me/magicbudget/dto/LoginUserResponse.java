package me.magicbudget.dto;

import java.util.UUID;

public record LoginUserResponse(UUID userId, String token) {

}
