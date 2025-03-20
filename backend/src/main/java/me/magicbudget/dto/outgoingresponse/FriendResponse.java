package me.magicbudget.dto.outgoingresponse;

import java.math.BigDecimal;

public record FriendResponse(String username,
                             String email,
                             String name,
                             BigDecimal amountOwed) {

}
