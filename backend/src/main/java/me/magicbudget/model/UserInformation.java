package me.magicbudget.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class UserInformation implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String email;

  private String username;

  private String password;

  private String firstName;

  private String lastName;

  @OneToOne(mappedBy = "information")
  @PrimaryKeyJoinColumn
  private User user;

  @OneToMany(mappedBy = "user")
  private List<Friendship> friendships;

  @ManyToMany(mappedBy = "members")
  private List<Group> groups;

  public UserInformation() {
  }


  public UserInformation(String username, String password, String firstName,
      String lastName, String email) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.friendships = new ArrayList<>();
    this.groups = new ArrayList<>();
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void addGroup(Group group) {
    groups.add(group);
  }

  public void addFriend(Friendship friendship) {
    friendships.add(friendship);
  }

  public List<Friendship> getFriendships() {
    return friendships;
  }

  public String getEmail() {
    return email;
  }


  public UUID getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    return "%s %s".formatted(firstName, lastName);
  }


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
