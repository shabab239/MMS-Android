package com.shabab.mezz.activity.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.shabab.mezz.activity.User.UserActivity;
import com.shabab.mezz.activity.bill.BillActivity;
import com.shabab.mezz.activity.meal.MealActivity;
import com.shabab.mezz.R;
import com.shabab.mezz.activity.purchase.PurchaseActivity;
import com.shabab.mezz.activity.transaction.TransactionActivity;
import com.shabab.mezz.activity.utility.UtilityActivity;
import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.api.service.ApiService;
import com.shabab.mezz.api.service.UserService;
import com.shabab.mezz.model.Mess;
import com.shabab.mezz.model.Transaction;
import com.shabab.mezz.model.User;
import com.shabab.mezz.util.Wait;

import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sp;
    User user;
    Mess mess;
    TextView messName;
    TextView messBalance;
    TextView totalMeals;
    TextView totalPurchase;
    TextView totalUtility;
    TextView monthTV;
    MaterialCardView mealBtn;
    MaterialCardView purchaseBtn;
    MaterialCardView utilityBtn;
    MaterialCardView transactionBtn;
    MaterialCardView billBtn;
    MaterialCardView userBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sp = getSharedPreferences("sp", MODE_PRIVATE);

        user = new Gson().fromJson(sp.getString("user", null), User.class);
        mess = user.getMess();

        messName = findViewById(R.id.messName);
        messBalance = findViewById(R.id.messBalance);
        totalMeals = findViewById(R.id.totalMeals);
        totalPurchase = findViewById(R.id.totalPurchase);
        totalUtility = findViewById(R.id.totalUtility);
        monthTV = findViewById(R.id.month);
        userBtn = findViewById(R.id.userBtn);

        mealBtn = findViewById(R.id.mealBtn);
        mealBtn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MealActivity.class));
        });

        purchaseBtn = findViewById(R.id.purchaseBtn);
        purchaseBtn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, PurchaseActivity.class));
        });

        utilityBtn = findViewById(R.id.utilityBtn);
        utilityBtn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, UtilityActivity.class));
        });

        transactionBtn = findViewById(R.id.transactionBtn);
        transactionBtn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TransactionActivity.class));
        });

        billBtn = findViewById(R.id.billBtn);
        billBtn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, BillActivity.class));
        });

        userBtn = findViewById(R.id.userBtn);
        userBtn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, UserActivity.class));
        });

        loadDashboardInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardInfo();
    }

    private void loadDashboardInfo() {
        Wait.show(this);
        UserService userService = ApiService.getService(UserService.class);

        Call<ApiResponse> call = userService.getDashboardInfo();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        Double totalPurchaseCost = (Double) apiResponse.getData("totalPurchaseCost");
                        Double totalUtilityCost = (Double) apiResponse.getData("totalUtilityCost");
                        Double totalMealCount = (Double) apiResponse.getData("totalMeals");
                        String month = (String) apiResponse.getData("month");
                        LinkedTreeMap<String, Object> messMap = (LinkedTreeMap<String, Object>) apiResponse.getData("mess");
                        String messJSON = new Gson().toJson(messMap);
                        Mess mess = new Gson().fromJson(messJSON, Mess.class);


                        totalPurchase.setText(String.format("Total Purchase: %.2f TK.", totalPurchaseCost));
                        totalUtility.setText(String.format("Total Utility Cost: %.2f TK.", totalUtilityCost));
                        totalMeals.setText(String.format("Total Meals: %.2f", totalMealCount));
                        monthTV.setText(month);
                        messName.setText(mess.getName());
                        messBalance.setText(mess.getBalance() + " TK.");
                    } else {
                        Toasty.error(HomeActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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