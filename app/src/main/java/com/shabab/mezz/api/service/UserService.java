package com.shabab.mezz.api.service;

import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/api/user/getAllUsers")
    Call<ApiResponse> getAllUsers();

}