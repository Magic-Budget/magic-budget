package me.magicbudget.controller;

import me.magicbudget.model.Category;
import me.magicbudget.model.CategoryTotals;
import me.magicbudget.model.ExpenseCategory;
import me.magicbudget.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/{userId}/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/totals")
  public ResponseEntity<List<CategoryTotals>> getTransactionByCategoryTotals(@PathVariable("userId") UUID userId) {
    List<CategoryTotals> categoryTotals = categoryService.getCategoryTotals(userId);
    return categoryTotals.isEmpty()
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(categoryTotals);
  }

  @GetMapping("/")
  public ResponseEntity<List<ExpenseCategory>> getCategories() {
    List<ExpenseCategory> categories = Arrays.asList(ExpenseCategory.values());
    return ResponseEntity.ok(categories);
  }
}
