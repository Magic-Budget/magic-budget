package me.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.model.CreditDebt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditDebtRepository extends JpaRepository<CreditDebt, UUID> {

  List<CreditDebt> findByUserId(UUID userId);

}
