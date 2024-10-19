package com.shabab.mezz.api.service;

import com.shabab.mezz.model.Bazar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BazarService {
    @GET("/api/bazar/getAllBazars")
    Call<List<Bazar>> getAllBazars();

    @GET("/api/bazar/getBazarById/{id}")
    Call<Bazar> getBazarById(@Path("id") int id);

    @POST("/api/bazar/createBazar")
    Call<Bazar> createBazar(@Body Bazar bazar);

    @PUT("/api/bazar/updateBazar/{id}")
    Call<Bazar> updateBazar(@Path("id") int id, @Body Bazar bazar);
}
