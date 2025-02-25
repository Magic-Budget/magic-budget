package me.magicbudget.magicbudget.repository;

import java.util.UUID;
import me.magicbudget.magicbudget.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
