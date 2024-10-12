package com.shabab.mezz.model;

public class LoginRequest {

    private String cell;
    private String password;

    public LoginRequest(String cell, String password) {
        this.cell = cell;
        this.password = password;
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

}
