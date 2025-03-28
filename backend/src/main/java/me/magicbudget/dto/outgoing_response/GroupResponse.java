package me.magicbudget.dto.outgoing_response;

import java.util.ArrayList;
import java.util.List;
import me.magicbudget.dto.BasicUserInformation;

public class GroupResponse {

  private final String groupName;

  private final List<BasicUserInformation> userInformation;

  public GroupResponse(String groupName) {
    this.groupName = groupName;
    this.userInformation = new ArrayList<>();
  }

  public void add(BasicUserInformation basicUserInformation) {
    this.userInformation.add(basicUserInformation);
  }

  public String getGroupName() {
    return groupName;
  }

  public List<BasicUserInformation> getUserInformation() {
    return userInformation;
  }
}
