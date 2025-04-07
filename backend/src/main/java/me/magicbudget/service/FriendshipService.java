package me.magicbudget.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.dto.BasicUserInformation;
import me.magicbudget.model.Friendship;
import me.magicbudget.model.UserInformation;
import me.magicbudget.repository.FriendshipRepository;
import me.magicbudget.repository.UserInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendshipService {

  @Autowired
  private final UserInformationRepository userRepository;

  @Autowired
  private final FriendshipRepository friendshipRepository;

  public FriendshipService(UserInformationRepository userRepository,
      FriendshipRepository friendshipRepository) {
    this.userRepository = userRepository;
    this.friendshipRepository = friendshipRepository;
  }

  @Transactional(rollbackFor = IllegalArgumentException.class)
  public void addFriend(UUID userId, String usernameToAdd) throws IllegalArgumentException {
    // Find the user by username
    Optional<UserInformation> friendOptional = Optional.ofNullable(
        userRepository.findByUsername(usernameToAdd));

    if (friendOptional.isEmpty()) {
      throw new IllegalArgumentException("Friend with username " + usernameToAdd + " not found.");
    }

    UserInformation friend = friendOptional.get();

    Optional<UserInformation> user = userRepository.findById(userId);

    if (user.isEmpty()) {
      throw new IllegalArgumentException("User with username " + userId + " not found.");
    }

    UserInformation currentUser = user.get();

    // Check if the current user is already friends with the user
    if (isAlreadyFriends(currentUser, friend)) {
      throw new IllegalArgumentException("You are already friends with " + usernameToAdd);
    }

    // Create a new friendship entity
    Friendship friendship1 = new Friendship();
    friendship1.setUser(currentUser);
    friendship1.setFriend(friend);

    Friendship friendship2 = new Friendship();
    friendship2.setUser(friend);
    friendship2.setFriend(currentUser);

    // Save both friendships
    friendshipRepository.save(friendship1);
    friendshipRepository.save(friendship2);

//    currentUser.getFriendships().add(friendship1);
    currentUser.addFriend(friendship1);
//    friend.getFriendships().add(friendship2);
    friend.addFriend(friendship2);

    // Save the users to update their relationships
    userRepository.save(currentUser);
    userRepository.save(friend);
  }

  /**
   * Checks if two users are already friends.
   */
  private boolean isAlreadyFriends(UserInformation user1, UserInformation user2) {
    List<Friendship> friendships = user1.getFriendships();

    for (Friendship friendship : friendships) {
      if (friendship.getFriend().equals(user2)) {
        return true; // already friends
      }
    }

    return false;
  }

  public List<BasicUserInformation> getFriends(UUID userId) throws IllegalArgumentException {

    Optional<UserInformation> user = userRepository.findById(userId);

    if (user.isEmpty()) {
      throw new IllegalArgumentException("User with username " + userId + " not found.");
    }

    UserInformation currentUser = user.get();

    List<Friendship> friendships = currentUser.getFriendships();
    List<BasicUserInformation> response = new ArrayList<>();
    for (Friendship friendship : friendships) {
      UserInformation friend = friendship.getFriend();
      BasicUserInformation friendResponse = UserService.getBasicInformation(friend);

      response.add(friendResponse);
    }

    return response;

  }
}
