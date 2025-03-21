package me.magicbudget.controller;

import me.magicbudget.dto.incoming_request.GoalCreateRequest;
import me.magicbudget.dto.outgoing_response.GoalResponse;
import me.magicbudget.service.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/{userid}/goals")
public class GoalController {

  private final GoalService goalService;

  public GoalController(GoalService goalService) {
    this.goalService = goalService;
  }

  @PostMapping
  public ResponseEntity<GoalResponse> createGoal(@PathVariable("userid") UUID userId,
      @RequestBody GoalCreateRequest request) {
    System.out.println("Creating goal for user: " + userId);
    return goalService.createGoal(userId, request.name(), request.target(), request.currentAmount(),
            request.due())
        .map(goal -> new GoalResponse(goal.id(), userId, goal.name(), goal.target(),
            goal.currentAmount(), goal.due()))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.badRequest().build());
  }

  @GetMapping("/{goalId}")
  public ResponseEntity<GoalResponse> getGoalById(@PathVariable("userid") UUID userId,
      @PathVariable UUID goalId) {
    return goalService.getGoalById(goalId)
        .map(goal -> new GoalResponse(goal.id(), userId, goal.name(), goal.target(),
            goal.currentAmount(), goal.due()))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<GoalResponse>> getGoalByUser(@PathVariable("userid") UUID userId) {
    var goals = goalService.getGoalsByUserId(userId)
        .stream()
        .map(goal -> new GoalResponse(goal.id(), userId, goal.name(), goal.target(),
            goal.currentAmount(), goal.due()))
        .toList();
    return ResponseEntity.ok(goals);
  }

  @PatchMapping("/{goalId}")
  public ResponseEntity<GoalResponse> updateGoal(@PathVariable("userid") UUID userId,
      @PathVariable UUID goalId, @RequestBody GoalCreateRequest request) {
    return goalService.updateGoal(goalId, request.name(), request.target(),request.currentAmount(), request.due())
        .map(goal -> new GoalResponse(goal.id(), userId, goal.name(), goal.target(),
            goal.currentAmount(), goal.due()))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.badRequest().build());
  }
}
