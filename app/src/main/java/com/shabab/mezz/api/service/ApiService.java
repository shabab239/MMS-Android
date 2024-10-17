package com.shabab.mezz.api.service;

import com.shabab.mezz.util.AuthInterceptor;
import com.shabab.mezz.util.TokenManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static Retrofit retrofit = null;
    private static TokenManager tokenManager = null;

    public static void setTokenManager(TokenManager manager) {
        if (tokenManager == null) {
            tokenManager = manager;
        }
    }

    private ApiService() {
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenManager))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static <T> T getService(Class<T> serviceClass) {
        return getRetrofitInstance().create(serviceClass);
    }
}
