package me.magicbudget.dto.outgoingresponse;

import java.math.BigDecimal;

public record ReceiptResponse(String image, BigDecimal amount) {

}
