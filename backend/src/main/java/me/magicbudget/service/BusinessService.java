package me.magicbudget.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.Business;
import me.magicbudget.repository.BusinessRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public final class BusinessService {

  private final BusinessRepository businessRepository;

  public BusinessService(BusinessRepository businessRepository) {
    this.businessRepository = businessRepository;
  }

  public Business createBusiness(@NonNull Business business) {
    return businessRepository.save(business);
  }

  public Optional<Business> getBusinessById(@NonNull UUID id) {
    return businessRepository.findById(id);
  }

  public List<Business> getAllBusinesses() {
    return businessRepository.findAll();
  }

  public Business updateBusiness(@NonNull Business business) {
    return businessRepository.save(business);
  }

  public void deleteBusiness(@NonNull UUID id) {
    businessRepository.deleteById(id);
  }
}