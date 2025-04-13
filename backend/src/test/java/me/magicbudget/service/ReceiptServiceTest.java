package me.magicbudget.service;

import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ReceiptServiceTest {

  @Autowired
  private ReceiptService receiptService;

  @Autowired
  private UserService userService;

  @Test
  void testUploadDocument() {
    User user = new User(new UserInformation("receiptuser1", "John", "Doe", "password123", ""));
    User savedUser = userService.createUser(user);

    MultipartFile file = new MockMultipartFile("receipt.jpg", "receipt.jpg", "image/jpeg",
        new byte[5]);

    Exception exception = assertThrows(Exception.class, () -> {
      receiptService.uploadDocument(savedUser.getId(), file);
    });
  }

  @Test
  void testFetchReceipts() {
    User user = new User(new UserInformation("receiptuser2", "Jane", "Smith", "password456", ""));
    User savedUser = userService.createUser(user);

    var receipts = receiptService.fetchReceipts(savedUser.getId());
    assertTrue(receipts.isEmpty(), "Expected no receipts for new user");
  }


}
