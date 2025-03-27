package me.magicbudget.repository;

import me.magicbudget.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {

  UserInformation findByUsername(String username);

}
