package com.shabab.mezz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
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
import com.shabab.mezz.api.service.MealService;
import com.shabab.mezz.model.MealRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private MealAdapter mealAdapter;
    private TextView datePicker;
    private int day;
    private int month;
    private int year;
    private List<MealRequest> mealRequestList;
    private Button recordMeals;

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


        final Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        datePicker = findViewById(R.id.datePicker);
        datePicker.setText(day + "-" + (month + 1) + "-" + year);
        datePicker.setOnClickListener(v -> {
            DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(day, month, year);
            datePickerFragment.setOnDateSetListener((view, selectedYear, selectedMonth, selectedDay) -> {
                day = selectedDay;
                month = selectedMonth;
                year = selectedYear;

                datePicker.setText(day + "-" + (month + 1) + "-" + year);
                getAndSetMeals();
            });
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        });


        recordMeals = findViewById(R.id.recordMeals);
        recordMeals.setOnClickListener(v -> {
            recordMeals();
        });

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        mealAdapter = new MealAdapter(new ArrayList<>(), MealActivity.this);
        recyclerViewUsers.setAdapter(mealAdapter);

        getAndSetMeals();
    }

    private void getAndSetMeals() {
        MealService mealService = ApiService.getService(MealService.class);

        Call<ApiResponse> call = mealService.getDailyMealRecords(day, month + 1, year);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Map<String, Object> data = apiResponse.getData();
                        mealRequestList = MealRequest.getDailyMealRecords(data, MealActivity.this);

                        mealAdapter = new MealAdapter(mealRequestList, MealActivity.this);
                        recyclerViewUsers.setAdapter(mealAdapter);
                    } else {
                        Toasty.error(MealActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(MealActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recordMeals() {
        MealService mealService = ApiService.getService(MealService.class);

        Call<ApiResponse> call = mealService.recordMeals(mealAdapter.getMealRequestList());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Toasty.success(MealActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(MealActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(MealActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private int day, month, year;
        private DatePickerDialog.OnDateSetListener onDateSetListener;

        public static DatePickerFragment newInstance(int day, int month, int year) {
            DatePickerFragment fragment = new DatePickerFragment();
            Bundle args = new Bundle();
            args.putInt("day", day);
            args.putInt("month", month);
            args.putInt("year", year);
            fragment.setArguments(args);
            return fragment;
        }

        public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
            this.onDateSetListener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (getArguments() != null) {
                day = getArguments().getInt("day");
                month = getArguments().getInt("month");
                year = getArguments().getInt("year");
            }
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (onDateSetListener != null) {
                onDateSetListener.onDateSet(view, year, month, day);
            }
        }
    }


}