package com.shabab.mezz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.api.service.ApiService;
import com.shabab.mezz.api.service.AuthService;
import com.shabab.mezz.api.service.MealService;
import com.shabab.mezz.api.service.UserService;
import com.shabab.mezz.model.MealRequest;
import com.shabab.mezz.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private TextView datePicker;

    private List<MealRequest> mealRequests;

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

        datePicker = findViewById(R.id.datePicker);
        datePicker.setOnClickListener(v -> {
            new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
        });

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        MealAdapter adapter = new MealAdapter(new ArrayList<>(), MealActivity.this);
        recyclerViewUsers.setAdapter(adapter);
        getAndSetUsers();
    }

    private void getAndSetUsers() {
        UserService userService = ApiService.getService(UserService.class);

        Call<ApiResponse> call = userService.getAllUsers();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Map<String, Object> data = apiResponse.getData();

                    List<User> userList = User.getUsers(data, MealActivity.this);

                    for (User user : userList) {
                        MealRequest mealRequest = new MealRequest();
                        mealRequest.setUserId(user.getId());
                        mealRequest.setMeals();
                    }

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

    private void recordMeals() {
        MealService mealService = ApiService.getService(MealService.class);

        Call<ApiResponse> call = mealService.recordMeals();
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date the user picks.
        }
    }

}