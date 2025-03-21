package me.magicbudget.dto.incoming_request;

import java.util.UUID;

public record ReceiptUpdateRequest(UUID receiptId, String amount) {

}
