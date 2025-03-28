package me.magicbudget.service;

import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.magicbudget.dto.incoming_request.ReceiptUpdateRequest;
import me.magicbudget.dto.outgoing_response.ReceiptResponse;
import me.magicbudget.model.Receipt;
import me.magicbudget.model.User;
import me.magicbudget.repository.ReceiptRepository;
import me.magicbudget.repository.UserRepository;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReceiptService {

  private static final String FILE_REGEX = "[^a-zA-Z0-9.-]";
  private static final String TOTAL_REGEX = "(total\\s*(after\\s*tax)?|grand\\s*total|amount\\s*due|final\\s*total)\\D*(\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})?)";
  private final UserRepository userRepository;
  private final ReceiptRepository receiptRepository;

  @Autowired
  public ReceiptService(UserRepository userRepository, ReceiptRepository receiptRepository) {
    this.userRepository = userRepository;
    this.receiptRepository = receiptRepository;
  }

  private static BigDecimal extractAmount(String input) {
    Pattern pattern = Pattern.compile("\\$([\\d,]+\\.?\\d*)");
    Matcher matcher = pattern.matcher(input);
    if (matcher.find()) {
      String amountStr = matcher.group(1).replace(",", "");
      return new BigDecimal(amountStr);
    }
    return null;
  }

  private static String convertImageToBase64(File imageFile) throws IOException {
    byte[] fileContent = Files.readAllBytes(imageFile.toPath());
    return Base64.getEncoder().encodeToString(fileContent);
  }

  /**
   * Uploads a document to the server. Then extracts the total amount from the document and saves it
   * to the database.
   * <p>
   * Must be transactional due to the use of LOB from Hibernate.
   *
   * @param userId the user id
   * @param file   the file to upload
   */
  @Transactional
  public void uploadDocument(@NonNull UUID userId, @NonNull MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    try {
      String originalFileName = file.getOriginalFilename();
      String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
      var tempFile = File.createTempFile(file.getName().replaceAll(FILE_REGEX, "_"), fileExtension);
      Files.write(tempFile.toPath(), file.getBytes());

      User user = userRepository.findById(userId)
          .orElseThrow(() -> new IllegalArgumentException("User not found"));

      var tesseract = new Tesseract();
      String extractedText = tesseract.doOCR(tempFile);

      Pattern pattern = Pattern.compile(TOTAL_REGEX, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(extractedText);

      BigDecimal totalAmount = null;

      while (matcher.find()) {
        totalAmount = extractAmount(matcher.group(0));
      }

      Receipt receipt = new Receipt(null, convertImageToBase64(tempFile), user, totalAmount);

      receiptRepository.save(receipt);
    } catch (Exception e) {
      throw new RuntimeException("Failed to save file", e);
    }
  }

  /**
   * Fetches receipts for the given user. Must be transactional due to the use of LOB from
   * Hibernate.
   *
   * @param userId the user id
   * @return a list of receipt responses
   */
  @Transactional
  public List<ReceiptResponse> fetchReceipts(UUID userId) {
    return receiptRepository.findByUserId(userId)
        .stream()
        .map(receipt -> new ReceiptResponse(receipt.getId(), receipt.getImage(),
            receipt.getAmount()))
        .collect(Collectors.toList());
  }

  /**
   * Updates a receipt with the given request. Must be transactional due to the use of LOB from
   * Hibernate.
   *
   * @param userId  the user id
   * @param request the request
   */
  @Transactional
  public void updateReceipt(UUID userId, ReceiptUpdateRequest request) {
    if (request.amount() == null) { // Only update the amount
      return;
    }

    Receipt receipt = receiptRepository.findById(request.receiptId())
        .orElseThrow(() -> new IllegalArgumentException("Receipt not found"));

    BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.amount()));

    receipt.setAmount(amount);

    receiptRepository.save(receipt);
  }
}