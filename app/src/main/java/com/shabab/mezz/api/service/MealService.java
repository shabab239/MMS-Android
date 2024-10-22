package com.shabab.mezz.api.service;

import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.dto.MealDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MealService {

    @GET("/api/meal/getDailyMealRecords")
    Call<ApiResponse> getDailyMealRecords(@Query("day") int day,
                                          @Query("month") int month,
                                          @Query("year") int year);

    @POST("/api/meal/recordMeals")
    Call<ApiResponse> recordMeals(@Body List<MealDTO> mealList);

}
