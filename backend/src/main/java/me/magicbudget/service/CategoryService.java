package me.magicbudget.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.Category;
import me.magicbudget.model.CategoryTotals;
import me.magicbudget.repository.CategoryRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
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

  public List<CategoryTotals> getCategoryTotals(@NonNull UUID userID) {
    return this.categoryRepository.findTotalAmountPerCategory(userID);
  }

  public List<Category> getAllCategories() {
    return this.categoryRepository.findAll();
  }
}
