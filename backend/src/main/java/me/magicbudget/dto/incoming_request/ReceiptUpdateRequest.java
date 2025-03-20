package me.magicbudget.dto.incomingrequest;

import java.util.UUID;

public record ReceiptUpdateRequest(UUID receiptId, String amount) {

}
