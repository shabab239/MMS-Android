package com.shabab.mezz.activity.bill;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.R;
import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.api.service.ApiService;
import com.shabab.mezz.api.service.BillService;
import com.shabab.mezz.model.Bill;
import com.shabab.mezz.model.Bill;
import com.shabab.mezz.util.Wait;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BillAdapter billAdapter;
    private TextView datePicker;
    private int day;
    private int month;
    private int year;
    private List<Bill> billList;
    private Button sendEmail;
    private Button processBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sendEmail = findViewById(R.id.sendEmail);
        sendEmail.setOnClickListener(v -> {
            Toasty.info(this, "Coming soon...", Toasty.LENGTH_SHORT, true).show();
            //sendEmail();
        });

        processBill = findViewById(R.id.processBill);
        processBill.setOnClickListener(v -> {
            processBill();
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        billAdapter = new BillAdapter(new ArrayList<>(), BillActivity.this);
        recyclerView.setAdapter(billAdapter);

        loadBills();
    }

    private void loadBills() {
        Wait.show(this);
        BillService billService = ApiService.getService(BillService.class);

        Call<ApiResponse> call = billService.getBills(10, 2024);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Map<String, Object> data = apiResponse.getData();
                        billList = Bill.getBills(data, BillActivity.this);

                        billAdapter = new BillAdapter(billList, BillActivity.this);
                        recyclerView.setAdapter(billAdapter);
                    } else {
                        Toasty.error(BillActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(BillActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void processBill() {
        Wait.show(this);
        BillService billService = ApiService.getService(BillService.class);

        Call<ApiResponse> call = billService.generateBill(10, 2024);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        loadBills();
                        Toasty.success(BillActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toasty.error(BillActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(BillActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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