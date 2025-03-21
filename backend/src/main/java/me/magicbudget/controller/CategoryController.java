package me.magicbudget.controller;

import me.magicbudget.model.CategoryTotals;
import me.magicbudget.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.List;
import java.util.UUID;

public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/categories/totals")
  public ResponseEntity<List<CategoryTotals>> getTransactionByCategoryTotals(@RequestHeader("X-User-Id") UUID userId) {
    List<CategoryTotals> categoryTotals = categoryService.getCategoryTotals(userId);
    return categoryTotals.isEmpty()
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok(categoryTotals);
  }
}
