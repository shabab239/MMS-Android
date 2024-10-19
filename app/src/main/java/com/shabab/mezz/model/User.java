package com.shabab.mezz.model;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {

    @SerializedName("id")
    private Long id;

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
    private Long messId;

    public static List<User> getUsers(Map<String, Object> data, Context context) {
        try {
            if (data != null && data.containsKey("users")) {
                List<User> userList = new ArrayList<>();

                Gson gson = new Gson();
                String jsonString = gson.toJson(data.get("users"));
                JSONArray usersArray = new JSONArray(jsonString);

                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userObject = usersArray.getJSONObject(i);
                    User user = gson.fromJson(userObject.toString(), User.class);
                    userList.add(user);
                }
                return userList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error parsing user data", Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<>();
    }

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getMessId() {
        return messId;
    }

    public void setMessId(Long messId) {
        this.messId = messId;
    }
}

