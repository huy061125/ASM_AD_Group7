package com.example.myapplication.model;

public class BudgetModel {
    private int id;
    private int userId;
    private String category;
    private double amount;
    private String monthYear;

    public BudgetModel(int id, int userId, String category, double amount, String monthYear) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.monthYear = monthYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}