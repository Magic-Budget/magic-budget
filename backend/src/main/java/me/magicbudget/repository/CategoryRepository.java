package me.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.model.Category;
import me.magicbudget.model.CategoryTotals;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

  @Query("SELECT new me.magicbudget.model.CategoryTotals(c.name, SUM(t.amount)) "
      + "FROM Category c JOIN Transaction t JOIN User u WHERE u.id = :userID "
      + "GROUP BY c.name")
  List<CategoryTotals> findTotalAmountPerCategory(@Param("userID") UUID userID);
}
