package com.shabab.mezz.api.service;

import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.model.Purchase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PurchaseService {

    @GET("/api/purchase/")
    Call<ApiResponse> getAllPurchases();

    @POST("/api/purchase/save")
    Call<ApiResponse> savePurchase(@Body Purchase purchase);

    @GET("/api/purchase/{id}")
    Call<ApiResponse> getPurchaseById(@Path("id") Long id);

    @DELETE("/api/purchase/{id}")
    Call<ApiResponse> deletePurchaseById(@Path("id") Long id);

}
