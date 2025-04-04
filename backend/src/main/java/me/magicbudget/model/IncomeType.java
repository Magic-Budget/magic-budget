package me.magicbudget.model;

public enum IncomeType {

  ONETIME(1), WEEKLY(7), BIWEEKLY(14), MONTHLY(30), YEARLY(365);

  private final int days;

  IncomeType(int days) {
    this.days = days;
  }

  public int getDays() {
    return days;
  }
}
