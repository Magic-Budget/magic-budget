package me.magicbudget.repository;

import me.magicbudget.model.Friendship;
import me.magicbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {

  List<Friendship> findByFriend(User user);

}
