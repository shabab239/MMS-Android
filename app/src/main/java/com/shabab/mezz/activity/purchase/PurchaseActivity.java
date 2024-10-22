package com.shabab.mezz.activity.purchase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
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

import com.google.android.material.textfield.TextInputEditText;
import com.shabab.mezz.R;
import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.api.service.ApiService;
import com.shabab.mezz.api.service.PurchaseService;
import com.shabab.mezz.api.service.UserService;
import com.shabab.mezz.model.Purchase;
import com.shabab.mezz.model.User;
import com.shabab.mezz.util.Wait;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PurchaseAdapter purchaseAdapter;
    private TextView datePicker;
    private int day;
    private int month;
    private int year;
    private List<Purchase> purchaseList;
    private Button addPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
            PurchaseActivity.DatePickerFragment datePickerFragment = PurchaseActivity.DatePickerFragment.newInstance(day, month, year);
            datePickerFragment.setOnDateSetListener((view, selectedYear, selectedMonth, selectedDay) -> {
                day = selectedDay;
                month = selectedMonth;
                year = selectedYear;

                datePicker.setText(day + "-" + (month + 1) + "-" + year);
                //getAndSetMeals(); //TODO Call API
            });
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        });


        addPurchase = findViewById(R.id.addPurchase);
        addPurchase.setOnClickListener(v -> {
            showAddPurchaseDialog();
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchaseAdapter = new PurchaseAdapter(new ArrayList<>(), PurchaseActivity.this);
        recyclerView.setAdapter(purchaseAdapter);

        loadPurchases();
    }

    private void showAddPurchaseDialog() {
        Dialog purchageDialog = new Dialog(this);
        purchageDialog.setContentView(R.layout.dialog_add_purchase);

        TextInputEditText editTextDescription = purchageDialog.findViewById(R.id.description);
        TextInputEditText editTextCost = purchageDialog.findViewById(R.id.cost);
        TextInputEditText editTextDate = purchageDialog.findViewById(R.id.date);
        Spinner spinnerUser = purchageDialog.findViewById(R.id.user);
        Button savePurchaseBtn = purchageDialog.findViewById(R.id.savePurchase);

        Button cancelBtn = purchageDialog.findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(v -> {
            if (purchageDialog.isShowing()) {
                purchageDialog.dismiss();
            }
        });

        loadUsers(spinnerUser);

        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        editTextDate.setText(day + "-" + (month + 1) + "-" + year);

        editTextDate.setOnClickListener(v -> {
            DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(day, month, year);
            datePickerFragment.setOnDateSetListener((view, selectedYear, selectedMonth, selectedDay) -> {
                day = selectedDay;
                month = selectedMonth;
                year = selectedYear;
                editTextDate.setText(day + "-" + (month + 1) + "-" + year);
            });
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        });

        savePurchaseBtn.setOnClickListener(v -> {
            String description = editTextDescription.getText().toString().trim();
            String costString = editTextCost.getText().toString().trim();
            int selectedUserPosition = spinnerUser.getSelectedItemPosition();

            if (description.isEmpty() || costString.isEmpty() || selectedUserPosition == AdapterView.INVALID_POSITION) {
                Toasty.error(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double cost = Double.parseDouble(costString);
            User selectedUser = (User) spinnerUser.getItemAtPosition(selectedUserPosition);

            Purchase newPurchase = new Purchase();
            newPurchase.setDescription(description);
            newPurchase.setCost(cost);
            calendar.set(year, month, day);
            newPurchase.setDate(calendar.getTime());
            newPurchase.setUser(new User(selectedUser.getId()));

            savePurchase(newPurchase);
            purchageDialog.dismiss();
        });

        purchageDialog.show();
    }

    private void loadUsers(Spinner spinner) {
        Wait.show(this);
        UserService userService = ApiService.getService(UserService.class);

        Call<ApiResponse> call = userService.getAllUsers();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        List<User> users = User.getUsers(apiResponse.getData(), PurchaseActivity.this);
                        ArrayAdapter<User> adapter = new ArrayAdapter<>(PurchaseActivity.this,
                                android.R.layout.simple_spinner_item, users);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    } else {
                        Toasty.error(PurchaseActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(PurchaseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void savePurchase(Purchase purchase) {
        Wait.show(this);
        PurchaseService purchaseService = ApiService.getService(PurchaseService.class);

        Call<ApiResponse> call = purchaseService.savePurchase(purchase);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Toasty.success(PurchaseActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        loadPurchases();
                    } else {
                        Toasty.error(PurchaseActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(PurchaseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void loadPurchases() {
        Wait.show(this);
        PurchaseService purchaseService = ApiService.getService(PurchaseService.class);

        Call<ApiResponse> call = purchaseService.getAllPurchases();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Map<String, Object> data = apiResponse.getData();
                        purchaseList = Purchase.getPurchases(data, PurchaseActivity.this);

                        purchaseAdapter = new PurchaseAdapter(purchaseList, PurchaseActivity.this);
                        recyclerView.setAdapter(purchaseAdapter);
                    } else {
                        Toasty.error(PurchaseActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(PurchaseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private int day, month, year;
        private DatePickerDialog.OnDateSetListener onDateSetListener;

        public static PurchaseActivity.DatePickerFragment newInstance(int day, int month, int year) {
            PurchaseActivity.DatePickerFragment fragment = new PurchaseActivity.DatePickerFragment();
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