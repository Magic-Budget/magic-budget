package me.magicbudget.repository;

import java.util.UUID;
import me.magicbudget.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {

  UserInformation findByUsername(String username);

}
