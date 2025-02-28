// This code was generated with Claud 3.7 Thinker inside VScode with
// the prompt "Create a test class for the BusinessService class".
package me.magicbudget.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.Business;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BusinessServiceTest {

  @Autowired
  private BusinessService businessService;
//
//  @Test
//  void testCreateBusiness() {
//    // Create a new business
//    Business business = new Business(null, "Test Company");
//    Business savedBusiness = businessService.createBusiness(business);
//
//    // Assertions
//    assertNotNull(savedBusiness.getId(), "Business ID should not be null");
//    assertEquals("Test Company", savedBusiness.getName(), "Business name should match");
//  }
//
//  @Test
//  void testCreateDuplicateBusiness() {
//    // Create first business
//    Business business1 = new Business(null, "Unique Business");
//    businessService.createBusiness(business1);
//
//    // Try to create another business with the same name
//    Business business2 = new Business(null, "Unique Business");
//
//    // Should throw exception due to unique constraint
//    assertThrows(DataIntegrityViolationException.class, () -> {
//      businessService.createBusiness(business2);
//    });
//  }
//
//  @Test
//  void testGetBusinessByGetId() {
//    // Create a business
//    Business business = new Business(null, "Sample Corp");
//    Business savedBusiness = businessService.createBusiness(business);
//
//    // Retrieve the business
//    Optional<Business> retrievedBusinessOpt = businessService.getBusinessById(
//        savedBusiness.getId());
//
//    // Assertions
//    assertTrue(retrievedBusinessOpt.isPresent(), "Business should be present");
//    Business retrievedBusiness = retrievedBusinessOpt.get();
//    assertEquals(savedBusiness.getId(), retrievedBusiness.getId(), "Business ID should match");
//    assertEquals("Sample Corp", retrievedBusiness.getName(), "Business name should match");
//  }
//
//  @Test
//  void testGetBusinessByNonExistentGetId() {
//    // Generate a random UUID
//    UUID nonExistentId = UUID.randomUUID();
//
//    // Try to retrieve non-existent business
//    Optional<Business> retrievedBusinessOpt = businessService.getBusinessById(nonExistentId);
//
//    // Assertions
//    assertFalse(retrievedBusinessOpt.isPresent(), "Business should not be present");
//  }
//
//  @Test
//  void testGetAllBusinesses() {
//    // Create a few businesses with unique names
//    Business business1 = new Business(null, "Company A");
//    Business business2 = new Business(null, "Company B");
//    Business business3 = new Business(null, "Company C");
//
//    businessService.createBusiness(business1);
//    businessService.createBusiness(business2);
//    businessService.createBusiness(business3);
//
//    // Get all businesses
//    List<Business> allBusinesses = businessService.getAllBusinesses();
//
//    // Assertions
//    assertNotNull(allBusinesses, "Business list should not be null");
//    assertTrue(allBusinesses.size() >= 3, "Should have at least 3 businesses");
//    assertTrue(allBusinesses.stream().anyMatch(b -> "Company A".equals(b.getName())),
//        "List should contain Company A");
//    assertTrue(allBusinesses.stream().anyMatch(b -> "Company B".equals(b.getName())),
//        "List should contain Company B");
//    assertTrue(allBusinesses.stream().anyMatch(b -> "Company C".equals(b.getName())),
//        "List should contain Company C");
//  }
//
//  @Test
//  void testUpdateBusiness() {
//    // Create a business
//    Business business = new Business(null, "Original Name");
//    Business savedBusiness = businessService.createBusiness(business);
//
//    // Update the business
//    savedBusiness.setName("Updated Name");
//    Business updatedBusiness = businessService.updateBusiness(savedBusiness);
//
//    // Retrieve the updated business
//    Optional<Business> retrievedBusinessOpt = businessService.getBusinessById(
//        updatedBusiness.getId());
//
//    // Assertions
//    assertTrue(retrievedBusinessOpt.isPresent(), "Business should be present");
//    Business retrievedBusiness = retrievedBusinessOpt.get();
//    assertEquals("Updated Name", retrievedBusiness.getName(), "Business name should be updated");
//  }
//
//  @Test
//  void testDeleteBusiness() {
//    // Create a business
//    Business business = new Business(null, "To Be Deleted Corp");
//    Business savedBusiness = businessService.createBusiness(business);
//
//    // Verify business exists
//    Optional<Business> businessBeforeDeletion = businessService.getBusinessById(
//        savedBusiness.getId());
//    assertTrue(businessBeforeDeletion.isPresent(), "Business should exist before deletion");
//
//    // Delete the business
//    businessService.deleteBusiness(savedBusiness.getId());
//
//    // Try to retrieve the deleted business
//    Optional<Business> businessAfterDeletion = businessService.getBusinessById(
//        savedBusiness.getId());
//
//    // Assertions
//    assertFalse(businessAfterDeletion.isPresent(), "Business should be deleted");
//  }
//
//  @Test
//  void testUpdateBusinessWithDuplicateGetName() {
//    // Create two businesses
//    Business business1 = new Business(null, "Existing Business");
//    Business business2 = new Business(null, "Different Name");
//
//    businessService.createBusiness(business1);
//    Business savedBusiness2 = businessService.createBusiness(business2);
//
//    // Try to update the second business with the first's name
//    savedBusiness2.setName("Existing Business");
//
//    // Should throw exception due to unique constraint
//    assertThrows(DataIntegrityViolationException.class, () -> {
//      businessService.updateBusiness(savedBusiness2);
//    });
//  }
}