package me.magicbudget.service;

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
import jakarta.transaction.Transactional;
import me.magicbudget.dto.outgoingresponse.ReceiptResponse;
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

  @Transactional
  public Receipt uploadDocument(@NonNull UUID userId, @NonNull MultipartFile file) {
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

      return receiptRepository.save(receipt);
    } catch (Exception e) {
      throw new RuntimeException("Failed to save file", e);
    }
  }

  @Transactional
  public List<ReceiptResponse> fetchReceipts(UUID userId) {
    return receiptRepository.findByUserId(userId)
        .stream()
        .map(receipt -> new ReceiptResponse(receipt.getImage(), receipt.getAmount()))
        .collect(Collectors.toList());
  }

  public static BigDecimal extractAmount(String input) {
    Pattern pattern = Pattern.compile("\\$([\\d,]+\\.?\\d*)");
    Matcher matcher = pattern.matcher(input);
    if (matcher.find()) {
      String amountStr = matcher.group(1).replace(",", "");
      return new BigDecimal(amountStr);
    }
    return null;
  }

  public static String convertImageToBase64(File imageFile) throws IOException {
    byte[] fileContent = Files.readAllBytes(imageFile.toPath());
    return Base64.getEncoder().encodeToString(fileContent);
  }
}