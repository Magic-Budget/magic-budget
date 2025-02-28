package me.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.model.Category;
import me.magicbudget.model.CategoryTotals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

  List<CategoryTotals> findTotalAmountPerCategory();
}
