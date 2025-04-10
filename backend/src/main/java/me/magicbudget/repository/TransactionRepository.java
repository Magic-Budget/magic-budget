package me.magicbudget.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import me.magicbudget.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {


  List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

}