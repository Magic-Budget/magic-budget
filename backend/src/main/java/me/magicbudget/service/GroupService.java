package me.magicbudget.service;

import jakarta.transaction.Transactional;
import me.magicbudget.dto.BasicUserInformation;
import me.magicbudget.dto.outgoing_response.GroupResponse;
import me.magicbudget.dto.outgoing_response.SplitTransactionResponse;
import me.magicbudget.model.Group;
import me.magicbudget.model.SplitTransaction;
import me.magicbudget.model.TransactionType;
import me.magicbudget.model.UserInformation;
import me.magicbudget.repository.GroupRepository;
import me.magicbudget.repository.SplitTransactionRepository;
import me.magicbudget.repository.UserInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

  @Autowired
  private final SplitTransactionRepository splitTransactionRepository;
  @Autowired
  private UserService userService;

  public GroupService(GroupRepository groupRepository,
      UserInformationRepository userRepository,
      SplitTransactionRepository splitTransactionRepository) {
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.splitTransactionRepository = splitTransactionRepository;
  }

  @Transactional(rollbackOn = IllegalArgumentException.class)
  public void createGroup(String groupName, UUID userId) {
    // Ensure the currentUser exists in the database
    UserInformation user = userRepository.findById(userId)
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


  @Transactional(rollbackOn = IllegalArgumentException.class)
  public void addUserToGroup(UUID currentUser,
      String otherUsername,
      String groupName) {

    UserInformation userInformation = userRepository.findById(currentUser)
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


  public List<GroupResponse> getGroups(UUID userid) {

    UserInformation userInformation = userRepository.findById(userid)
        .orElseThrow(() ->
            new IllegalArgumentException("User not found"));

    List<GroupResponse> responses = new ArrayList<>();

    List<Group> groups = userInformation.getGroups();
    for (Group group : groups) {
      GroupResponse groupResponse = new GroupResponse(group.getGroupName());

      group.getMembers().forEach(member -> {
        BasicUserInformation basicUserInformation =  UserService.getBasicInformation(member);
        groupResponse.add(basicUserInformation);
      });

      responses.add(groupResponse);
    }
    return responses;
  }

  @Transactional(rollbackOn = IllegalArgumentException.class)
  public void addTransaction(UUID userid,
      String transactionName,
      LocalDateTime transactionDate,
      BigDecimal amount,
      String description,
      List<String> splitWith,
      String groupName) {

    UserInformation paidBy = userRepository.findById(userid)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    List<Group> groups = paidBy.getGroups();

    Group group = groups.stream()
        .filter(g -> g.getGroupName().equals(groupName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Group not found"));

    SplitTransaction transaction = new SplitTransaction(transactionName,
        transactionDate,
        amount,
        description,
        TransactionType.SPILT,
        paidBy);

    transaction.setGroup(group);

    List<UserInformation> usersToAdd = new ArrayList<>();
    for (String splitUserId : splitWith) {
      UserInformation userToSplit = userRepository.findById(UUID.fromString(splitUserId))
          .orElseThrow(() -> new IllegalArgumentException("User " + splitUserId + " not found"));
      usersToAdd.add(userToSplit);
    }

    transaction.setOwedTo(usersToAdd);

    group.getTransactions().add(transaction);

    splitTransactionRepository.save(transaction);

    groupRepository.save(group);

  }

  public List<SplitTransactionResponse> getTransactions(UUID userid, String groupName) {

    UserInformation user = userRepository.findById(userid)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    Optional<Group> groupOptional = user.getGroups().stream()
        .filter(g -> g.getGroupName().equals(groupName)).findFirst();

    if (groupOptional.isEmpty()) {
      throw new IllegalArgumentException("Group not found");
    }

    Group group = groupOptional.get();

    List<SplitTransaction> transactions = group.getTransactions();

    List<SplitTransactionResponse> transactionResponses = new ArrayList<>();
    for (SplitTransaction transaction : transactions) {
      SplitTransactionResponse response = new SplitTransactionResponse(
          transaction.getId(),
          transaction.getName(),
          transaction.getTransactionDate(),
          transaction.getAmount(),
          transaction.getDescription(),
          transaction.getPaidBy().getUsername()
      );

      for (UserInformation userInTransaction : transaction.getOwedTo()) {
        response.getUsers().add(userInTransaction.getUsername());
      }

      transactionResponses.add(response);
    }

    return transactionResponses;

  }
}
