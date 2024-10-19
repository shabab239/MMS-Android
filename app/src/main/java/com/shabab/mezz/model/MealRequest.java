package com.shabab.mezz.model;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MealRequest {
    private Long userId;
    private String username;
    private int day;
    private int meals;
    private int month;
    private int year;

    public MealRequest() {
    }

    public MealRequest(Long userId, String username, int day, int meals, int month, int year) {
        this.userId = userId;
        this.username = username;
        this.day = day;
        this.meals = meals;
        this.month = month;
        this.year = year;
    }

    public static List<MealRequest> getDailyMealRecords(Map<String, Object> data, Context context) {
        try {
            if (data != null && data.containsKey("meals")) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(data.get("meals"));

                Type listType = new TypeToken<List<MealRequest>>() {}.getType();
                return gson.fromJson(jsonString, listType);
            }
        } catch (JsonSyntaxException e) {
            Toast.makeText(context, "Error parsing meal data", Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<>();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

