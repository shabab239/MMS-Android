package com.shabab.mezz.api.service;

import com.shabab.mezz.api.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BillService {

    @POST("/api/bill/generateBill")
    Call<ApiResponse> generateBill(
            @Query("month") Integer month,
            @Query("year") Integer year
    );

    @GET("/api/bill/")
    Call<ApiResponse> getBills(
            @Query("month") Integer month,
            @Query("year") Integer year
    );
}
