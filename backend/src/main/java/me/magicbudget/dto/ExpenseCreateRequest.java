package me.magicbudget.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import me.magicbudget.model.Business;

public class ExpenseCreateRequest {

    private Business business;
    
    public ExpenseCreateRequest(String name, LocalDateTime transactionDate,
    BigDecimal amount, String description, Business business){
        this.business = business;
    }

    public Business getBusiness(){
        return business;
    }

    public void setBusiness(Business business){
        this.business = business;
    }
}
