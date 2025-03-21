package me.magicbudget.repository;

import me.magicbudget.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
  List<Receipt> findByUserId(UUID userId);
}
