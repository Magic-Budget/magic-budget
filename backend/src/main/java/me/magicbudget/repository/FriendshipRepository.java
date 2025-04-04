package me.magicbudget.repository;

import java.util.List;
import java.util.UUID;
import me.magicbudget.model.Friendship;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {

  List<Friendship> findByFriend(User user);

}
