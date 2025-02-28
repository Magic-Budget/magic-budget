package me.magicbudget.repository;

import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  UserDetails findUserByUsername(String username);

}
