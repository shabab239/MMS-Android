package com.shabab.mezz.api;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Map<String, Object> data;

    @SerializedName("errors")
    private Map<String, String> errors;

    @SerializedName("successful")
    private boolean successful;

    public ApiResponse() {}

    public ApiResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Object getData(String key) {
        if (this.data == null) {
            return null;
        }
        return this.data.get(key);
    }

    public void setData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
    }
}