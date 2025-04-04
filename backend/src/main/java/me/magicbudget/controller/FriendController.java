package me.magicbudget.controller;


import java.util.List;
import java.util.UUID;
import me.magicbudget.dto.BasicUserInformation;
import me.magicbudget.dto.incoming_request.FriendAddRequest;
import me.magicbudget.service.FriendshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/{userid}/friend")
public class FriendController {

  private final FriendshipService friendshipService;

  public FriendController(FriendshipService friendshipService) {
    this.friendshipService = friendshipService;
  }

  @PostMapping("/add-friend")
  public ResponseEntity<String> addFriend(@PathVariable("userid") UUID userId,
      @RequestBody FriendAddRequest request) {
    try {
      friendshipService.addFriend(userId, request.username());
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/")
  public ResponseEntity<List<BasicUserInformation>> getFriends(
      @PathVariable("userid") UUID userId) {
    try {
      return new ResponseEntity<>(friendshipService.getFriends(userId), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

}
