package me.magicbudget.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.dto.incoming_request.CreateBusinessRequest;
import me.magicbudget.model.Business;
import me.magicbudget.model.User;
import me.magicbudget.service.BusinessService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

  private final BusinessService businessService;
  private final UserService userService;

  public BusinessController(BusinessService businessService, UserService userService) {
    this.businessService = businessService;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<Business> createBusiness(@RequestBody CreateBusinessRequest request,
      @RequestHeader("X-User-Id") UUID userId) {
    try {
      User user = userService.getUserById(userId)
          .orElse(null);

      if (user == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      Business business = new Business(null, request.getName(), user);
      Business createdBusiness = businessService.createBusiness(business);
      return new ResponseEntity<>(createdBusiness, HttpStatus.CREATED);
    } catch (DataIntegrityViolationException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Business> getBusinessById(@PathVariable UUID id,
      @RequestHeader("X-User-Id") UUID userId) {
    Optional<Business> business = businessService.getBusinessById(id, userId);
    return business.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<List<Business>> getAllBusinesses(@RequestHeader("X-User-Id") UUID userId) {
    List<Business> businesses = businessService.getAllBusinesses(userId);
    return new ResponseEntity<>(businesses, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Business> updateBusiness(@PathVariable UUID id,
      @RequestHeader("X-User-Id") UUID userId,
      @RequestBody Business businessRequest) {
    Optional<Business> existingBusinessOpt = businessService.getBusinessById(id, userId);
    if (existingBusinessOpt.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Business existingBusiness = existingBusinessOpt.get();
    if (businessRequest.getName() != null) {
      existingBusiness.setName(businessRequest.getName());
    }

    Business updatedBusiness = businessService.updateBusiness(existingBusiness);
    return new ResponseEntity<>(updatedBusiness, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBusiness(@PathVariable UUID id,
      @RequestHeader("X-User-Id") UUID userId) {
    Optional<Business> business = businessService.getBusinessById(id, userId);
    if (business.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    businessService.deleteBusiness(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}