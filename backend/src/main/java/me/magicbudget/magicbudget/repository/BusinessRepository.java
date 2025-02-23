package me.magicbudget.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.magicbudget.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {

}
