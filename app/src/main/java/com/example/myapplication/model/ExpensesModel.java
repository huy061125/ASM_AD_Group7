package com.example.myapplication.model;

public class ExpensesModel {
    private int id;
    private int userId;
    private String description; // Sửa lỗi chính tả
    private String category;
    private double amount;
    private String date;

    public ExpensesModel(int id, int userId, String description, String category, double amount, String date) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description; // Sửa lỗi chính tả
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDescription() { // Sửa lỗi chính tả
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
