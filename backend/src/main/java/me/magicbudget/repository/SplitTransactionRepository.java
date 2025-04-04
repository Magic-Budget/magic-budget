package me.magicbudget.repository;

import java.util.UUID;
import me.magicbudget.model.SplitTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SplitTransactionRepository extends JpaRepository<SplitTransaction, UUID> {

}
