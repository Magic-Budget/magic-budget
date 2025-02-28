package me.magicbudget.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.magicbudget.model.Business;
import me.magicbudget.model.User;
import me.magicbudget.repository.BusinessRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public final class BusinessService {

  private final BusinessRepository businessRepository;
  private final UserService userService;

  public BusinessService(BusinessRepository businessRepository, UserService userService) {
    this.businessRepository = businessRepository;
    this.userService = userService;
  }

  public Business createBusiness(@NonNull Business business) {
    return businessRepository.save(business);
  }

  public Optional<Business> getBusinessById(@NonNull UUID id, @NonNull UUID userId) {
    User user = userService.getUserById(userId)
        .orElse(null);

    if (user == null) {
      return Optional.empty();
    }
    return businessRepository.findBusinessByUser(user)
        .stream()
        .filter(b -> b.getId().equals(id))
        .findFirst();
  }

  public List<Business> getAllBusinesses(@NonNull UUID userId) {
    return businessRepository.findBusinessByUser(userService.getUserById(userId).orElse(null));
  }

  public Business updateBusiness(@NonNull Business business) {
    return businessRepository.save(business);
  }

  public void deleteBusiness(@NonNull UUID id) {
    businessRepository.deleteById(id);
  }
}