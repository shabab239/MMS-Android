package com.shabab.mezz.api.service;

import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.model.Transaction;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TransactionService {

    @GET("/api/transaction/")
    Call<ApiResponse> getAllTransactions();

    @POST("/api/transaction/save")
    Call<ApiResponse> saveTransaction(@Body Transaction transaction);

    @GET("/api/transaction/{id}")
    Call<ApiResponse> getTransactionById(@Path("id") Long id);

    @DELETE("/api/transaction/{id}")
    Call<ApiResponse> deleteTransactionById(@Path("id") Long id);
}
