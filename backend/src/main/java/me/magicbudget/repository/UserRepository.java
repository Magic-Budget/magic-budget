package me.magicbudget.repository;

import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.User;
import me.magicbudget.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findUserByInformation(UserInformation information);

}
