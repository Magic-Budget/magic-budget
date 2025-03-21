package me.magicbudget.repository;

import me.magicbudget.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public interface GroupRepository extends JpaRepository<Group, UUID> {

  List<Group> findByGroupName(String groupName);

}
