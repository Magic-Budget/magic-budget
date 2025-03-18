package me.magicbudget.dto.outgoingresponse;

import java.math.BigDecimal;

public record ReceiptResponse(byte[] image, BigDecimal amount) {

}
