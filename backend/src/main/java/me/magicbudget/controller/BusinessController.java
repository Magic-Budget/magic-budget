package me.magicbudget.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.Business;
import me.magicbudget.service.BusinessService;
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
@RequestMapping("/api/businesses")
public class BusinessController {

  private final BusinessService businessService;

  public BusinessController(BusinessService businessService) {
    this.businessService = businessService;
  }

  @PostMapping
  public ResponseEntity<Business> createBusiness(@RequestBody Business business) {
    try {
      Business createdBusiness = businessService.createBusiness(business);
      return new ResponseEntity<>(createdBusiness, HttpStatus.CREATED);
    } catch (DataIntegrityViolationException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Business> getBusinessById(@PathVariable UUID id) {
    Optional<Business> business = businessService.getBusinessById(id);
    return business.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<List<Business>> getAllBusinesses() {
    List<Business> businesses = businessService.getAllBusinesses();
    return new ResponseEntity<>(businesses, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Business> updateBusiness(@PathVariable UUID id,
      @RequestBody Business businessRequest) {
    Optional<Business> existingBusinessOpt = businessService.getBusinessById(id);
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
  public ResponseEntity<Void> deleteBusiness(@PathVariable UUID id) {
    Optional<Business> business = businessService.getBusinessById(id);
    if (business.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    businessService.deleteBusiness(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}