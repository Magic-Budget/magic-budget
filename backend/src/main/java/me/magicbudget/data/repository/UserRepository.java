package me.magicbudget.data.repository;

import me.magicbudget.data.entities.AuthUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AuthUsers, UUID> {
  AuthUsers findUserByUsername(String username);
}
