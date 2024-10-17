package com.shabab.mezz.util;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (originalRequest.url().encodedPath().contains("/login")) {
            return chain.proceed(originalRequest);
        }

        String token = tokenManager.getToken();
        if (token != null) {
            Request.Builder builder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token);
            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(originalRequest);
    }
}

