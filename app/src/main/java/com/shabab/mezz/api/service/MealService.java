package com.shabab.mezz.api.service;

import com.shabab.mezz.model.MealRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MealService {

    @POST("api/meal/recordMeals")
    Call<Void> recordMeals(@Body List<MealRequest> mealRequests);

}
