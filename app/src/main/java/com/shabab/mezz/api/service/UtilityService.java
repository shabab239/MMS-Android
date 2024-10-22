package com.shabab.mezz.api.service;

import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.model.Purchase;
import com.shabab.mezz.model.Utility;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UtilityService {

    @GET("/api/utility/")
    Call<ApiResponse> getAllUtilities();

    @POST("/api/utility/save")
    Call<ApiResponse> saveUtility(@Body Utility utility);

    @GET("/api/utility/{id}")
    Call<ApiResponse> getUtilityById(@Path("id") Long id);

    @DELETE("/api/utility/{id}")
    Call<ApiResponse> deleteUtilityById(@Path("id") Long id);

}
