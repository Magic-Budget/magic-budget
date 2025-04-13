package me.magicbudget.service;

import me.magicbudget.model.CategoryTotals;
import me.magicbudget.repository.CategoryRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<CategoryTotals> getCategoryTotals(@NonNull UUID userID) {
    return this.categoryRepository.findTotalAmountPerCategory(userID);
  }

}
