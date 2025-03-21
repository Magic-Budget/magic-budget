package me.magicbudget.dto.outgoing_response;

import me.magicbudget.dto.BasicUserInformation;
import java.util.ArrayList;
import java.util.List;

public class GroupResponse {

  private final String groupName;

  private final List<BasicUserInformation> userInformation;

  public GroupResponse(String groupName) {
    this.groupName = groupName;
    this.userInformation = new ArrayList<>();
  }

  public void add (BasicUserInformation basicUserInformation) {
    this.userInformation.add(basicUserInformation);
  }

  public String getGroupName() {
    return groupName;
  }

  public List<BasicUserInformation> getUserInformation() {
    return userInformation;
  }
}
