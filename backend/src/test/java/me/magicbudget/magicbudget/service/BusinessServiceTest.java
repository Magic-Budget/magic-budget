package me.magicbudget.magicbudget.service;

import me.magicbudget.magicbudget.model.Business;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BusinessServiceTest {

    @Autowired
    private BusinessService businessService;

    @Test
    void testCreateBusiness() {
        // Create a new business
        Business business = new Business(null, "Test Company");
        Business savedBusiness = businessService.createBusiness(business);
        
        // Assertions
        assertNotNull(savedBusiness.id(), "Business ID should not be null");
        assertEquals("Test Company", savedBusiness.name(), "Business name should match");
    }
    
    @Test
    void testCreateDuplicateBusiness() {
        // Create first business
        Business business1 = new Business(null, "Unique Business");
        businessService.createBusiness(business1);
        
        // Try to create another business with the same name
        Business business2 = new Business(null, "Unique Business");
        
        // Should throw exception due to unique constraint
        assertThrows(DataIntegrityViolationException.class, () -> {
            businessService.createBusiness(business2);
        });
    }
    
    @Test
    void testGetBusinessById() {
        // Create a business
        Business business = new Business(null, "Sample Corp");
        Business savedBusiness = businessService.createBusiness(business);
        
        // Retrieve the business
        Optional<Business> retrievedBusinessOpt = businessService.getBusinessById(savedBusiness.id());
        
        // Assertions
        assertTrue(retrievedBusinessOpt.isPresent(), "Business should be present");
        Business retrievedBusiness = retrievedBusinessOpt.get();
        assertEquals(savedBusiness.id(), retrievedBusiness.id(), "Business ID should match");
        assertEquals("Sample Corp", retrievedBusiness.name(), "Business name should match");
    }
    
    @Test
    void testGetBusinessByNonExistentId() {
        // Generate a random UUID
        UUID nonExistentId = UUID.randomUUID();
        
        // Try to retrieve non-existent business
        Optional<Business> retrievedBusinessOpt = businessService.getBusinessById(nonExistentId);
        
        // Assertions
        assertFalse(retrievedBusinessOpt.isPresent(), "Business should not be present");
    }
    
    @Test
    void testGetAllBusinesses() {
        // Create a few businesses with unique names
        Business business1 = new Business(null, "Company A");
        Business business2 = new Business(null, "Company B");
        Business business3 = new Business(null, "Company C");
        
        businessService.createBusiness(business1);
        businessService.createBusiness(business2);
        businessService.createBusiness(business3);
        
        // Get all businesses
        List<Business> allBusinesses = businessService.getAllBusinesses();
        
        // Assertions
        assertNotNull(allBusinesses, "Business list should not be null");
        assertTrue(allBusinesses.size() >= 3, "Should have at least 3 businesses");
        assertTrue(allBusinesses.stream().anyMatch(b -> "Company A".equals(b.name())), 
                "List should contain Company A");
        assertTrue(allBusinesses.stream().anyMatch(b -> "Company B".equals(b.name())), 
                "List should contain Company B");
        assertTrue(allBusinesses.stream().anyMatch(b -> "Company C".equals(b.name())), 
                "List should contain Company C");
    }
    
    @Test
    void testUpdateBusiness() {
        // Create a business
        Business business = new Business(null, "Original Name");
        Business savedBusiness = businessService.createBusiness(business);
        
        // Update the business
        savedBusiness.setName("Updated Name");
        Business updatedBusiness = businessService.updateBusiness(savedBusiness);
        
        // Retrieve the updated business
        Optional<Business> retrievedBusinessOpt = businessService.getBusinessById(updatedBusiness.id());
        
        // Assertions
        assertTrue(retrievedBusinessOpt.isPresent(), "Business should be present");
        Business retrievedBusiness = retrievedBusinessOpt.get();
        assertEquals("Updated Name", retrievedBusiness.name(), "Business name should be updated");
    }
    
    @Test
    void testDeleteBusiness() {
        // Create a business
        Business business = new Business(null, "To Be Deleted Corp");
        Business savedBusiness = businessService.createBusiness(business);
        
        // Verify business exists
        Optional<Business> businessBeforeDeletion = businessService.getBusinessById(savedBusiness.id());
        assertTrue(businessBeforeDeletion.isPresent(), "Business should exist before deletion");
        
        // Delete the business
        businessService.deleteBusiness(savedBusiness.id());
        
        // Try to retrieve the deleted business
        Optional<Business> businessAfterDeletion = businessService.getBusinessById(savedBusiness.id());
        
        // Assertions
        assertFalse(businessAfterDeletion.isPresent(), "Business should be deleted");
    }
    
    @Test
    void testUpdateBusinessWithDuplicateName() {
        // Create two businesses
        Business business1 = new Business(null, "Existing Business");
        Business business2 = new Business(null, "Different Name");
        
        businessService.createBusiness(business1);
        Business savedBusiness2 = businessService.createBusiness(business2);
        
        // Try to update the second business with the first's name
        savedBusiness2.setName("Existing Business");
        
        // Should throw exception due to unique constraint
        assertThrows(DataIntegrityViolationException.class, () -> {
            businessService.updateBusiness(savedBusiness2);
        });
    }
}