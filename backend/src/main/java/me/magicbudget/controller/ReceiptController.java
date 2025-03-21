package me.magicbudget.controller;

import me.magicbudget.dto.incomingrequest.ReceiptUpdateRequest;
import me.magicbudget.dto.outgoing_response.ReceiptResponse;
import me.magicbudget.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PatchMapping
  public ResponseEntity<Void> editReceipt(@PathVariable("userid") UUID userId,
      @RequestBody ReceiptUpdateRequest request) {
    try {
      System.out.println("Updating receipt for user: " + userId);
      System.out.println(request.receiptId());
      receiptService.updateReceipt(userId, request);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves receipts from the server.
   *
   * @return a response entities with the receipt images
   */
  @GetMapping
  public List<ReceiptResponse> fetchReceipts(@PathVariable("userid") UUID userId) {
    return receiptService.fetchReceipts(userId);
  }
}
