package me.magicbudget.dto.outgoing_response;

public record StatsResponse(double averageIncome, double averageExpense, double totalTarget,
                            double totalAchieved) {

}
