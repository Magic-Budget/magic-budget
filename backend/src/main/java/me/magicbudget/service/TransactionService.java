//package me.magicbudget.service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import me.magicbudget.model.Transaction;
//import me.magicbudget.repository.TransactionRepository;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Service;
//
//@Service
//public final class TransactionService {
//
//  private final TransactionRepository transactionRepository;
//
//  public TransactionService(TransactionRepository transactionService) {
//    this.transactionRepository = transactionService;
//  }
//
//  public Transaction createTransaction(@NonNull Transaction transaction) {
//    return transactionRepository.save(transaction);
//  }
//
//  public Optional<Transaction> getTransactionById(@NonNull UUID transactionId) {
//    return transactionRepository.findById(transactionId);
//  }
//
//  public Transaction updateTransaction(@NonNull Transaction transaction) {
//    return transactionRepository.save(transaction);
//  }
//
//  public List<Transaction> getTransactionsByUserId(@NonNull UUID userId) {
//    return transactionRepository.findByUserId(userId);
//  }
//
//  public List<Transaction> getTransactionsByTransactionDateBetween(@NonNull LocalDateTime start,
//      @NonNull LocalDateTime end) {
//    return transactionRepository.findByTransactionDateBetween(start, end);
//  }
//}
