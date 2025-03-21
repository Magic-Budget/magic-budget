package me.magicbudget.dto.outgoing_response;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents the response to a receipt request.
 */
public record ReceiptResponse(UUID receiptId, String image, BigDecimal amount) {

}
