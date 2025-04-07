package me.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {

  List<Receipt> findByUserId(UUID userId);
}
