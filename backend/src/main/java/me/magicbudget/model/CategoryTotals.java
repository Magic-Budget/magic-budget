package me.magicbudget.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class CategoryTotals {

  @JsonProperty("category")
  private String category;

  @JsonProperty("total_amount")
  private BigDecimal totalAmount;

  public CategoryTotals(String category, BigDecimal totalAmount) {
    this.category = category;
    this.totalAmount = totalAmount;
  }
}
