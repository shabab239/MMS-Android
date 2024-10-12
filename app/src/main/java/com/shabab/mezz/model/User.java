package com.shabab.mezz.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class User {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("cell")
    private String cell;

    @SerializedName("password")
    private String password;

    @SerializedName("role")
    private UserRole role;

    @SerializedName("balance")
    private Double balance;

/*    @SerializedName("meals")
    private List<Meal> meals;

    @SerializedName("bazars")
    private List<Bazar> bazars;

    @SerializedName("bills")
    private List<Bill> bills;

    @SerializedName("transactions")
    private List<Transaction> transactions;*/

    @SerializedName("messId")
    private Integer messId;

    public enum UserRole {
        ADMIN("ADMIN"),
        MANAGER("MANAGER"),
        MEMBER("MEMBER");

        private final String role;

        UserRole(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    // Constructor
    public User() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getMessId() {
        return messId;
    }

    public void setMessId(Integer messId) {
        this.messId = messId;
    }
}

