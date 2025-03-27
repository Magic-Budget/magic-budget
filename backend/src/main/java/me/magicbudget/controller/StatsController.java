package me.magicbudget.controller;

import me.magicbudget.dto.outgoing_response.StatsResponse;
import me.magicbudget.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("api/{userid}/stats")
public final class StatsController {

  private final StatsService statsService;

  public StatsController(StatsService statsService) {
    this.statsService = statsService;
  }

  /**
   * Retrieves statistics for the user.
   *
   * @return a response entity with a status code
   */
  @GetMapping
  public ResponseEntity<StatsResponse> getStats(@PathVariable("userid") UUID userId) {
    StatsResponse stats = statsService.getStats(userId);
    return ResponseEntity.ok(stats);
  }
}