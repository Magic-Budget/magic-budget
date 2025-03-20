package me.magicbudget.service;


import me.magicbudget.dto.outgoingresponse.FriendResponse;
import me.magicbudget.model.Friendship;
import me.magicbudget.model.UserInformation;
import me.magicbudget.repository.FriendshipRepository;
import me.magicbudget.repository.UserInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

  public void addFriend(String userId, String usernameToAdd) {
    // Find the user by username
    Optional<UserInformation> friendOptional = Optional.ofNullable(
        userRepository.findByUsername(usernameToAdd));

    if (friendOptional.isEmpty()) {
      throw new IllegalArgumentException("Friend with username " + usernameToAdd + " not found.");
    }

    UserInformation friend = friendOptional.get();

    Optional<UserInformation> user = userRepository.findById(UUID.fromString(userId));

    if(user.isEmpty()){
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

  public List<FriendResponse> getFriends(String userId) {

    Optional<UserInformation> user = userRepository.findById(UUID.fromString(userId));

    if(user.isEmpty()){
      throw new IllegalArgumentException("User with username " + userId + " not found.");
    }

    UserInformation currentUser = user.get();

    List<Friendship> friendships = currentUser.getFriendships();
    List<FriendResponse> response = new ArrayList<>();
    for(Friendship friendship : friendships){
      UserInformation friend = friendship.getFriend();
      FriendResponse friendResponse = new FriendResponse(friend.getUsername(),
          friend.getEmail(),
          friend.getFirstName() + " " +
              friend.getLastName(),
          new BigDecimal(-10));

      response.add(friendResponse);
    }

    return response;

  }
}
