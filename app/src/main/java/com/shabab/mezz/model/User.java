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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private Role role;

    @SerializedName("balance")
    private Double balance;

    @SerializedName("mess")
    private Mess mess;

    public User(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

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

    public enum Role {
        ROLE_ADMIN,
        ROLE_MANAGER,
        ROLE_MEMBER
    }

}

