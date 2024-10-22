package com.shabab.mezz.activity.utility;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.shabab.mezz.R;
import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.api.service.ApiService;
import com.shabab.mezz.api.service.UtilityService;
import com.shabab.mezz.model.Utility;
import com.shabab.mezz.util.Wait;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UtilityAdapter utilityAdapter;
    private Button addUtility;
    private List<Utility> utilityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addUtility = findViewById(R.id.addUtility);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        utilityAdapter = new UtilityAdapter(new ArrayList<>(), UtilityActivity.this);
        recyclerView.setAdapter(utilityAdapter);

        addUtility.setOnClickListener(v -> showAddUtilityDialog());

        loadUtilities();
    }

    private void showAddUtilityDialog() {
        Dialog utilityDialog = new Dialog(this);
        utilityDialog.setContentView(R.layout.dialog_add_utility);

        Spinner monthSpinner = utilityDialog.findViewById(R.id.monthSpinner);
        TextInputEditText editTextMonth = utilityDialog.findViewById(R.id.month);
        TextInputEditText editTextYear = utilityDialog.findViewById(R.id.year);
        TextInputEditText editTextCost = utilityDialog.findViewById(R.id.cost);
        Button saveUtilityBtn = utilityDialog.findViewById(R.id.saveUtility);
        Button cancelBtn = utilityDialog.findViewById(R.id.cancel);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getMonthNames());
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        cancelBtn.setOnClickListener(v -> utilityDialog.dismiss());

        saveUtilityBtn.setOnClickListener(v -> {
            String monthString = editTextMonth.getText().toString().trim();
            String yearString = editTextYear.getText().toString().trim();
            String costString = editTextCost.getText().toString().trim();

            if (monthString.isEmpty() || yearString.isEmpty() || costString.isEmpty()) {
                Toasty.error(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int month = Integer.parseInt(monthString);
            int year = Integer.parseInt(yearString);
            double cost = Double.parseDouble(costString);

            Utility newUtility = new Utility();
            newUtility.setMonth(month);
            newUtility.setYear(year);
            newUtility.setCost(cost);

            saveUtility(newUtility);
            utilityDialog.dismiss();
        });

        utilityDialog.show();
    }

    private List<String> getMonthNames() {
        List<String> monthNames = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.MONTH, i);
            monthNames.add(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }
        return monthNames;
    }

    private void saveUtility(Utility utility) {
        Wait.show(this);
        UtilityService utilityService = ApiService.getService(UtilityService.class);

        Call<ApiResponse> call = utilityService.saveUtility(utility);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Toasty.success(UtilityActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        loadUtilities();
                    } else {
                        Toasty.error(UtilityActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(UtilityActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                Wait.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Wait.dismiss();
                Toasty.error(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUtilities() {
        Wait.show(this);
        UtilityService utilityService = ApiService.getService(UtilityService.class);

        Call<ApiResponse> call = utilityService.getAllUtilities();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Map<String, Object> data = apiResponse.getData();
                        utilityList = Utility.getUtilities(data, UtilityActivity.this);

                        utilityAdapter = new UtilityAdapter(utilityList, UtilityActivity.this);
                        recyclerView.setAdapter(utilityAdapter);
                    } else {
                        Toasty.error(UtilityActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(UtilityActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                Wait.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Wait.dismiss();
                Toasty.error(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
