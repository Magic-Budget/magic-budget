package me.magicbudget.magicbudget.service;

import me.magicbudget.magicbudget.model.Category;
import me.magicbudget.magicbudget.model.CategoryTotals;
import me.magicbudget.magicbudget.model.Category;
import me.magicbudget.magicbudget.repository.CategoryRepository;
import org.springframework.lang.NonNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryService {
  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public Category createCategory(@NonNull Category category) {
    return categoryRepository.save(category);
  }
  public Optional<Category> getTransactionById(@NonNull UUID categoryID) {
    return this.categoryRepository.findById(categoryID);
  }

  public Category updateTransaction(@NonNull Category transaction) {
    return this.categoryRepository.save(transaction);
  }

  public List<CategoryTotals> getCategoryTotals() {
    return this.categoryRepository.findTotalAmountPerCategory();
  }
}
