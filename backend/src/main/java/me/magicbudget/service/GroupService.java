package me.magicbudget.service;

import me.magicbudget.dto.BasicUserInformation;
import me.magicbudget.dto.outgoingresponse.GroupResponse;
import me.magicbudget.model.Group;
import me.magicbudget.model.UserInformation;
import me.magicbudget.repository.GroupRepository;
import me.magicbudget.repository.UserInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService {

  @Autowired
  private final GroupRepository groupRepository;

  @Autowired
  private final UserInformationRepository userRepository;

  public GroupService(GroupRepository groupRepository,
      UserInformationRepository userRepository) {
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
  }

  public void createGroup(String groupName, String userId) {
    // Ensure the currentUser exists in the database
    UserInformation user = userRepository.findById(UUID.fromString(userId))
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // Check if the user is already in a group with the same name

    List<Group> groups = user.getGroups();
    boolean alreadyInGroup = groups.stream()
        .anyMatch(group -> group.getGroupName().equalsIgnoreCase(groupName));

    if (alreadyInGroup) {
      throw new IllegalArgumentException("User is already a member of a group with the same name: " + groupName);
    }

    // Create a new group
    Group group = new Group(groupName);

    // Add the user to the group and maintain bi-directional consistency
    group.addMember(user);
    user.addGroup(group);

    // Save the group and the updated user to the database
    groupRepository.save(group);
    userRepository.save(user);
  }


  public void addUserToGroup(String currentUser,
      String otherUsername,
      String groupName) {

    UserInformation userInformation = userRepository.findById(UUID.fromString(currentUser))
        .orElseThrow(() ->
            new IllegalArgumentException("User not found"));

    Optional<Group> groupOptional = userInformation.getGroups().stream()
        .filter(g -> g.getGroupName().equalsIgnoreCase(groupName))
        .findFirst();

    if (groupOptional.isEmpty()) {
      throw new IllegalArgumentException("Group not found");
    }

    Group group = groupOptional.get();

    UserInformation otherUser = userRepository.findByUsername(otherUsername);

    if (!areUsersFriends(userInformation, otherUser)) {
      throw new IllegalArgumentException("User must be friends with the current user to be added to the group");
    }

    if (group.getMembers().contains(otherUser)) {
      throw new IllegalArgumentException("User is already a member of the group");
    }

    group.getMembers().add(otherUser);

    groupRepository.save(group);

    otherUser.getGroups().add(group);
    userRepository.save(otherUser);
  }
  private boolean areUsersFriends(UserInformation currentUser, UserInformation otherUser) {
    return currentUser.getFriendships().stream()
        .anyMatch(friendship -> friendship.getFriend().equals(otherUser));
  }


  public List<GroupResponse> getGroups(String userid) {

    UserInformation userInformation = userRepository.findById(UUID.fromString(userid))
        .orElseThrow(() ->
            new IllegalArgumentException("User not found"));

    List<GroupResponse> responses = new ArrayList<>();

    List<Group> groups = userInformation.getGroups();
    for (Group group : groups) {
      GroupResponse groupResponse = new GroupResponse(group.getGroupName());

      group.getMembers().forEach(member -> {
        BasicUserInformation basicUserInformation =
            new BasicUserInformation(member.getUsername(),
            member.getFirstName() + " " + member.getLastName(),
            member.getEmail());
        groupResponse.add(basicUserInformation);
      });

      responses.add(groupResponse);
    }
    return responses;
  }
}
