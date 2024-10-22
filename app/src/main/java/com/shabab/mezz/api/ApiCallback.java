package com.shabab.mezz.api;

import android.app.ActivityManager;
import android.content.SharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() == null) {
            onNullResponse();
        } else {
            onSuccess(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(call, t);
    }

    protected abstract void onSuccess(Call<T> call, Response<T> response);
    protected abstract void onError(Call<T> call, Throwable t);

    private void onNullResponse() {
        logout();
    }

    private void logout() {

    }
}

