package me.magicbudget.controller;


import me.magicbudget.dto.BasicUserInformation;
import me.magicbudget.dto.outgoing_response.FriendResponse;
import me.magicbudget.model.Friendship;
import me.magicbudget.service.FriendshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/{userid}/friend")
public class FriendController {

  private final FriendshipService friendshipService;

  public FriendController(FriendshipService friendshipService) {
    this.friendshipService = friendshipService;
  }

  @PostMapping("/add-friend")
  public ResponseEntity<String> addFriend(@PathVariable("userid") UUID userId, @RequestBody String username) {
    try {
      friendshipService.addFriend(userId, username);
      return new ResponseEntity<>(HttpStatus.OK);
    }
    catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/")
  public ResponseEntity<List<BasicUserInformation>> getFriends(@PathVariable("userid") UUID userId) {
    try {
      return new ResponseEntity<>(friendshipService.getFriends(userId),HttpStatus.OK);
    }
    catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }



}
