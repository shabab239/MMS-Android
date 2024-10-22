package com.shabab.mezz.activity.transaction;

import static androidx.core.content.ContentProviderCompat.requireContext;

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
import com.shabab.mezz.api.service.TransactionService;
import com.shabab.mezz.api.service.UserService;
import com.shabab.mezz.api.service.UtilityService;
import com.shabab.mezz.model.Transaction;
import com.shabab.mezz.model.User;
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


public class TransactionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private TextView datePicker;
    private int day;
    private int month;
    private int year;
    private List<Transaction> transactionList;
    private Button addTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction);
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
            DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(day, month, year);
            datePickerFragment.setOnDateSetListener((view, selectedYear, selectedMonth, selectedDay) -> {
                day = selectedDay;
                month = selectedMonth;
                year = selectedYear;

                datePicker.setText(day + "-" + (month + 1) + "-" + year);
            });
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        });

        addTransaction = findViewById(R.id.addTransaction);
        addTransaction.setOnClickListener(v -> {
            showAddTransactionDialog();
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionAdapter = new TransactionAdapter(new ArrayList<>(), TransactionActivity.this);
        recyclerView.setAdapter(transactionAdapter);

        loadTransactions();
    }

    private void showAddTransactionDialog() {
        Dialog transactionDialog = new Dialog(this);
        transactionDialog.setContentView(R.layout.dialog_add_transaction);

        TextInputEditText editTextAmount = transactionDialog.findViewById(R.id.amount);
        TextInputEditText editTextDate = transactionDialog.findViewById(R.id.date);
        Spinner spinnerUser = transactionDialog.findViewById(R.id.user);
        Button saveTransactionBtn = transactionDialog.findViewById(R.id.saveTransaction);

        Button cancelBtn = transactionDialog.findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(v -> {
            if (transactionDialog.isShowing()) {
                transactionDialog.dismiss();
            }
        });

        loadUsers(spinnerUser);

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

        saveTransactionBtn.setOnClickListener(v -> {
            String amountString = editTextAmount.getText().toString().trim();
            int selectedUserPosition = spinnerUser.getSelectedItemPosition();

            if (amountString.isEmpty() || selectedUserPosition == AdapterView.INVALID_POSITION) {
                Toasty.error(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountString);
            User user = (User) spinnerUser.getItemAtPosition(selectedUserPosition);

            Transaction newTransaction = new Transaction();
            newTransaction.setAmount(amount);
            newTransaction.setType(Transaction.TransactionType.DEPOSIT);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            newTransaction.setDate(calendar.getTime());
            newTransaction.setUser(new User(user.getId()));

            saveTransaction(newTransaction);
            transactionDialog.dismiss();
        });

        transactionDialog.show();
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
                        List<User> users = User.getUsers(apiResponse.getData(), TransactionActivity.this);
                        ArrayAdapter<User> adapter = new ArrayAdapter<>(TransactionActivity.this,
                                android.R.layout.simple_spinner_item, users);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    } else {
                        Toasty.error(TransactionActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(TransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void saveTransaction(Transaction transaction) {
        Wait.show(this);
        TransactionService transactionService = ApiService.getService(TransactionService.class);

        Call<ApiResponse> call = transactionService.saveTransaction(transaction);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Toasty.success(TransactionActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        loadTransactions();
                    } else {
                        Toasty.error(TransactionActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(TransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void loadTransactions() {
        Wait.show(this);
        TransactionService transactionService = ApiService.getService(TransactionService.class);

        Call<ApiResponse> call = transactionService.getAllTransactions();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Map<String, Object> data = apiResponse.getData();
                        transactionList = Transaction.getTransactions(data, TransactionActivity.this);

                        transactionAdapter = new TransactionAdapter(transactionList, TransactionActivity.this);
                        recyclerView.setAdapter(transactionAdapter);
                    } else {
                        Toasty.error(TransactionActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(TransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

        public static TransactionActivity.DatePickerFragment newInstance(int day, int month, int year) {
            TransactionActivity.DatePickerFragment fragment = new TransactionActivity.DatePickerFragment();
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
