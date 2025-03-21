package me.magicbudget.repository;

import me.magicbudget.model.SplitTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SplitTransactionRepository extends JpaRepository<SplitTransaction, UUID> {

}
