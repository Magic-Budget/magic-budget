package me.magicbudget.repository;

import java.util.UUID;
import me.magicbudget.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {

}
