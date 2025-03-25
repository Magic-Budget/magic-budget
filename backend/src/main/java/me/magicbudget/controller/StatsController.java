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
public class StatsController {
  private final StatsServ
  /**
   * Retrieves statistics for the user.
   *
   * @return a response entity with a status code
   */
  @GetMapping
  public ResponseEntity<StatsResponse> getStats(@PathVariable("userid") UUID userId) {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}