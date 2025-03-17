package me.magicbudget.dto.incomingrequest;

import jakarta.annotation.Nonnull;

public class CreateBusinessRequest {
    
    @Nonnull
    private String name;
    
    public CreateBusinessRequest(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
