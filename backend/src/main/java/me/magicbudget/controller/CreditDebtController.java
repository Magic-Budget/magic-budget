package me.magicbudget.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import me.magicbudget.dto.CreditDebtCreateRequest;
import me.magicbudget.model.Business;
import me.magicbudget.model.CreditDebt;
import me.magicbudget.model.User;
import me.magicbudget.service.BusinessService;
import me.magicbudget.service.CreditDebtService;
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
@RequestMapping("/api/credit-debts")
public class CreditDebtController {

  private final CreditDebtService creditDebtService;
  private final UserService userService;
  private final BusinessService businessService;

  public CreditDebtController(CreditDebtService creditDebtService, UserService userService,
      BusinessService businessService) {
    this.creditDebtService = creditDebtService;
    this.userService = userService;
    this.businessService = businessService;
  }

  @PostMapping("/{id}")
  public ResponseEntity<CreditDebt> createCreditDebt(@PathVariable UUID id,
      @RequestBody CreditDebtCreateRequest request) {
    try {
      return userService.getUserById(id)
          .flatMap(user -> businessService.getBusinessById(request.getBusinessId())
              .map(business -> {
                CreditDebt creditDebt = new CreditDebt(
                    null,
                    business,
                    user,
                    request.getAmount(),
                    request.getInterestRate()
                );

                CreditDebt createdCreditDebt = creditDebtService.createCreditDebt(creditDebt, id);
                return new ResponseEntity<>(createdCreditDebt, HttpStatus.CREATED);
              }))
          .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (DataIntegrityViolationException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<CreditDebt> getCreditDebtById(@PathVariable UUID id) {
    Optional<CreditDebt> creditDebt = creditDebtService.getCreditDebtById(id);
    return creditDebt.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<CreditDebt>> getCreditDebtsByUserId(@PathVariable UUID id) {
    List<CreditDebt> creditDebts = creditDebtService.getCreditDebtsByUserId(id);
    return new ResponseEntity<>(creditDebts, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CreditDebt> updateCreditDebt(@PathVariable UUID id,
      @RequestBody CreditDebtCreateRequest request) {
    try {
      Optional<CreditDebt> existingCreditDebtOpt = creditDebtService.getCreditDebtById(id);
      if (existingCreditDebtOpt.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      CreditDebt existingCreditDebt = existingCreditDebtOpt.get();

      if (request.getBusinessId() != null) {
        Optional<Business> businessOptional = businessService.getBusinessById(
            request.getBusinessId());

        if (businessOptional.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        existingCreditDebt.setBusiness(businessOptional.get());
      }
      if (request.getAmount() != null) {
        existingCreditDebt.setAmount(request.getAmount());
      }
      if (request.getInterestRate() != null) {
        existingCreditDebt.setInterestRate(request.getInterestRate());
      }

      CreditDebt updatedCreditDebt = creditDebtService.updateCreditDebt(existingCreditDebt);
      return new ResponseEntity<>(updatedCreditDebt, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCreditDebt(@PathVariable UUID id) {
    try {
      Optional<CreditDebt> creditDebtOpt = creditDebtService.getCreditDebtById(id);
      if (creditDebtOpt.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      creditDebtService.deleteCreditDebt(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity<Void> deleteAllCreditDebtsByUserId(@PathVariable UUID id) {
    try {
      creditDebtService.deleteAllCreditDebtsByUserId(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}