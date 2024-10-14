package com.shabab.mezz;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.api.service.UserService;
import com.shabab.mezz.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.meal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize DatePicker
        datePicker = findViewById(R.id.datePicker);

        // Initialize RecyclerView and set adapter
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        MealAdapter adapter = new MealAdapter(new ArrayList<>(), MealActivity.this);
        recyclerViewUsers.setAdapter(adapter);
        getAndSetUsers();
    }

    private void getAndSetUsers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);

        Call<ApiResponse> call = userService.getAllUsers();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Map<String, Object> data = apiResponse.getData();

                    List<User> userList = User.getUsers(data, MealActivity.this);

                    MealAdapter adapter = new MealAdapter(userList, MealActivity.this);
                    recyclerViewUsers.setAdapter(adapter);
                } else {
                    Toast.makeText(MealActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}