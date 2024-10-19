package com.shabab.mezz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.api.service.ApiService;
import com.shabab.mezz.api.service.AuthService;
import com.shabab.mezz.model.LoginRequest;
import com.shabab.mezz.model.Mess;
import com.shabab.mezz.model.User;
import com.shabab.mezz.util.TokenManager;

import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText cellEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginBtn;
    private SharedPreferences sp;
    private Retrofit retrofit;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cellEditText = findViewById(R.id.cell);
        passwordEditText = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        cellEditText.setText("01700000000");
        passwordEditText.setText("123456");

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authService = retrofit.create(AuthService.class);

        sp = getSharedPreferences("sp", MODE_PRIVATE);

        TokenManager tokenManager = new TokenManager(getApplicationContext());
        if (tokenManager.getToken() != null || !tokenManager.getToken().isEmpty()) {
            Call<ApiResponse> call = authService.isLoggedIn("Bearer " + tokenManager.getToken());
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    try {
                        ApiResponse apiResponse = response.body();
                        if (apiResponse.isSuccessful()) {
                            ApiService.setTokenManager(tokenManager);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                    } catch (Exception e) {}
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toasty.warning(getApplicationContext(), "Please login again", Toast.LENGTH_LONG).show();
                }
            });
        }

        loginBtn.setOnClickListener(v -> {
            login();
        });
    }

    private void login() {
        String cell = cellEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(cell)) {
            cellEditText.setError("Mobile Number is required");
            cellEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(cell, password);

        Call<ApiResponse> call = authService.login(loginRequest);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                 try {
                     ApiResponse apiResponse = response.body();
                     if (apiResponse.isSuccessful()) {
                         String token = (String) apiResponse.getData("token");

                         TokenManager tokenManager = new TokenManager(getApplicationContext());
                         ApiService.setTokenManager(tokenManager);
                         tokenManager.saveToken(token);

                         Map<String, Object> userMap = (Map<String, Object>) apiResponse.getData("user");
                         Map<String, Object> messMap = (Map<String, Object>) apiResponse.getData("mess");

                         SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
                         SharedPreferences.Editor editor = sp.edit();
                         editor.putString("user", new Gson().toJson(userMap));
                         editor.putString("mess", new Gson().toJson(messMap));
                         editor.apply();

                         startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                     } else {
                         Toasty.error(getApplicationContext(), apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                     }
                 } catch (Exception e) {
                     Toasty.error(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                 }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}