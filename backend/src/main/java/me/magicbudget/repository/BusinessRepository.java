package me.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.model.Business;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {

  List<Business> findBusinessByUser(User user);
}
