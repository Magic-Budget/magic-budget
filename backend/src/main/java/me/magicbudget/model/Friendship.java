package me.magicbudget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class Friendship {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserInformation user;

  @ManyToOne
  @JoinColumn(name = "friend_id")
  private UserInformation friend;


  public Friendship() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UserInformation getUser() {
    return user;
  }

  public void setUser(UserInformation user) {
    this.user = user;
  }

  public UserInformation getFriend() {
    return friend;
  }

  public void setFriend(UserInformation friend) {
    this.friend = friend;
  }
}
