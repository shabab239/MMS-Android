package com.shabab.mezz.model;

public class MealRequest {
    private Long userId;
    private int day;
    private int meals;
    private int month;
    private int year;

    public MealRequest() {
    }

    public MealRequest(Long userId, int day, int meals, int month, int year) {
        this.userId = userId;
        this.day = day;
        this.meals = meals;
        this.month = month;
        this.year = year;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMeals() {
        return meals;
    }

    public void setMeals(int meals) {
        this.meals = meals;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

