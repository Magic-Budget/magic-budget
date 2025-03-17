package me.magicbudget.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.dto.incomingrequest.SavingGoalCreateRequest;
import me.magicbudget.model.SavingGoal;
import me.magicbudget.model.User;
import me.magicbudget.service.SavingGoalService;
import me.magicbudget.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saving-goals")
public final class SavingGoalController {

  private final SavingGoalService savingGoalService;
  private final UserService userService;

  public SavingGoalController(SavingGoalService savingGoalService, UserService userService) {
    this.savingGoalService = savingGoalService;
    this.userService = userService;
  }

  @PostMapping("/{id}")
  public ResponseEntity<SavingGoal> createSavingGoal(@PathVariable UUID id,
      @RequestBody SavingGoalCreateRequest newGoal) {
    try {
      User user = userService.getUserById(id)
          .orElse(null);

      if (user == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      SavingGoal savingGoal = new SavingGoal();
      savingGoal.setName(newGoal.getName());
      savingGoal.setAmount(newGoal.getAmount());

      SavingGoal createdGoal = savingGoalService.createSavingGoal(savingGoal, id);
      return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
    } catch (DataIntegrityViolationException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<SavingGoal> getSavingGoalById(@PathVariable UUID id) {
    Optional<SavingGoal> savingGoal = savingGoalService.getSavingGoalById(id);
    return savingGoal.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<SavingGoal>> getSavingGoalsByid(@PathVariable UUID id) {
    List<SavingGoal> savingGoals = savingGoalService.getSavingGoalsByUserId(id);
    return new ResponseEntity<>(savingGoals, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<SavingGoal> updateSavingGoal(@PathVariable UUID id,
      @RequestBody SavingGoalCreateRequest goalRequest) {
    Optional<SavingGoal> existingGoalOpt = savingGoalService.getSavingGoalById(id);
    if (existingGoalOpt.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    SavingGoal existingGoal = existingGoalOpt.get();

    if (goalRequest.getName() != null) {
      existingGoal.setName(goalRequest.getName());
    }
    if (goalRequest.getAmount() != null) {
      existingGoal.setAmount(goalRequest.getAmount());
    }

    SavingGoal updatedGoal = savingGoalService.updateSavingGoal(existingGoal);
    return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSavingGoal(@PathVariable UUID id) {
    Optional<SavingGoal> existingGoal = savingGoalService.getSavingGoalById(id);
    if (existingGoal.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    savingGoalService.deleteSavingGoal(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
