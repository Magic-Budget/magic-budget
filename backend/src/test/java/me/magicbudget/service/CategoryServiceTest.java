package me.magicbudget.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import me.magicbudget.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import me.magicbudget.model.CategoryTotals;
import me.magicbudget.model.UserInformation;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Test
    void testGetCategoryTotals() {
        // Create a test user
        User user = new User(new UserInformation("testuser5", "password123", "Clark", "Kent", ""));
        User savedUser = userService.createUser(user);
        
        List<CategoryTotals> totals = categoryService.getCategoryTotals(savedUser.getId());

        assertNotNull(totals, "Category totals list should not be null");
    }
}
