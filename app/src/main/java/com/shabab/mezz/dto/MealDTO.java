package com.shabab.mezz.dto;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Project: Mezz-SpringBoot
 * Author: Shabab-1281539
 * Created on: 20/10/2024
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MealDTO {

    private Long userId;

    private String username;

    private Double meals;

    private Integer day;

    private Integer month;

    private Integer year;

    public static List<MealDTO> getDailyMealRecords(Map<String, Object> data, Context context) {
        try {
            if (data != null && data.containsKey("meals")) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(data.get("meals"));

                Type listType = new TypeToken<List<MealDTO>>() {}.getType();
                return gson.fromJson(jsonString, listType);
            }
        } catch (JsonSyntaxException e) {
            Toast.makeText(context, "Error parsing meal data", Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<>();
    }

}
