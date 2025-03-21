package me.magicbudget.controller;


import me.magicbudget.dto.incoming_request.AddUserToGroupRequest;
import me.magicbudget.dto.incoming_request.TransactionRequest;
import me.magicbudget.dto.outgoing_response.GroupResponse;
import me.magicbudget.dto.outgoing_response.SplitTransactionResponse;
import me.magicbudget.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/{userid}/group")
public class GroupController {

  private final GroupService groupService;

  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  @PostMapping("/create-group")
  public ResponseEntity<String> createGroup(@PathVariable("userid") UUID userId,
      @RequestBody String groupName) {
    try {
      groupService.createGroup(groupName, userId);
      return new ResponseEntity<>(HttpStatus.CREATED);
    }
    catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/add-user")
  public ResponseEntity<String> addUserToGroup(@PathVariable("userid") UUID userid,
      @RequestBody AddUserToGroupRequest request) {
    try {
      groupService.addUserToGroup(userid,
          request.otherUsername(),
          request.groupName());
      return new ResponseEntity<>(HttpStatus.CREATED);
    }
    catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

  }

  @GetMapping("/")
  public ResponseEntity<List<GroupResponse>> getGroups(@PathVariable UUID userid) {
    try {
      return new ResponseEntity<>(groupService.getGroups(userid),HttpStatus.OK);
    }
    catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/add-transaction")
  public ResponseEntity<String> addTransaction(
      @PathVariable UUID userid ,
      @RequestBody TransactionRequest transactionRequest) {

    try{
      String transactionName = transactionRequest.getName();
      LocalDateTime transactionDate = transactionRequest.getTransactionDate();
      BigDecimal amount = transactionRequest.getAmount();
      String description = transactionRequest.getDescription();

      List<String> usernames = transactionRequest.getUsers();

      String groupName = transactionRequest.getGroupName();

      groupService.addTransaction(userid,
          transactionName,
          transactionDate,
          amount,
          description,
          usernames,
          groupName);
      return new ResponseEntity<>(HttpStatus.CREATED);
    }
    catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/{group}/get-transactions")
  public ResponseEntity<List<SplitTransactionResponse>> getTransaction(@PathVariable UUID userid,
      @PathVariable(name = "group") String groupName) {

    try{
      return new ResponseEntity<>(groupService.getTransactions(userid,groupName),HttpStatus.OK);
    }
    catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

  }

}
