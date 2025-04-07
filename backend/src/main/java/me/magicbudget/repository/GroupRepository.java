package me.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface GroupRepository extends JpaRepository<Group, UUID> {

  List<Group> findByGroupName(String groupName);

}
