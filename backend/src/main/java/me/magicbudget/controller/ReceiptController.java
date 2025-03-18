package me.magicbudget.controller;

import me.magicbudget.dto.outgoingresponse.ReceiptResponse;
import me.magicbudget.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/{userid}/receipt")
public class ReceiptController {

  private final ReceiptService receiptService;

  @Autowired
  public ReceiptController(ReceiptService receiptService) {
    this.receiptService = receiptService;
  }

  /**
   * Uploads a receipt image to the server to be processed.
   *
   * @param file the image file to upload
   * @return a response entity with a status code
   */
  @PostMapping
  public ResponseEntity<Void> uploadReceipt(@PathVariable("userid") UUID userId,
      @RequestParam("file") MultipartFile file) {
    receiptService.uploadDocument(userId, file);
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves receipts from the server.
   *
   * @return a response entities with the receipt images
   */
  public List<ReceiptResponse> fetchReceipts(@PathVariable("userid") UUID userId) {
    return receiptService.fetchReceipts(userId);
  }
}
