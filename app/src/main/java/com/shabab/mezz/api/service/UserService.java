package com.shabab.mezz.api.service;

import com.shabab.mezz.api.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("/api/user/")
    Call<ApiResponse> getAllUsers();

}