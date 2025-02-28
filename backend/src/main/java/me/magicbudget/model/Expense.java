package me.magicbudget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DiscriminatorValue("EXPENSE")
public class Expense extends Transaction {
    
    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    //Constructors
    public Expense(){
    }

    public Expense(UUID id, User user, String name, LocalDateTime transactionDate,
    BigDecimal amount, String description, Business business){
        super(id, user, name, transactionDate, amount, description);
        this.business = business;
    }

    //Methods

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business){
        this.business = business;
    }

}
