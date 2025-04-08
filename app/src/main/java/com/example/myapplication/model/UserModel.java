package com.example.myapplication.model;

import android.database.sqlite.SQLiteDatabase;

public class UserModel {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private int roleID;

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
    public int getRoleID() {
        return roleID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

}
