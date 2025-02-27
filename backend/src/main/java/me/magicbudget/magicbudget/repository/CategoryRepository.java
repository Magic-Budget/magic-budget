package me.magicbudget.magicbudget.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.magicbudget.model.Category;
import me.magicbudget.magicbudget.model.CategoryTotals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
  Optional<Category> findCategoryById(UUID id);
  List<Category> getCategories();
  List<CategoryTotals> findTotalAmountPerCategory();
  List<CategoryTotals> findTotalAmountByCategory(UUID categoryId);
}
