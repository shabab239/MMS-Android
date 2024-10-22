package com.shabab.mezz.activity.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.shabab.mezz.activity.bill.BillActivity;
import com.shabab.mezz.activity.meal.MealActivity;
import com.shabab.mezz.R;
import com.shabab.mezz.activity.purchase.PurchaseActivity;
import com.shabab.mezz.activity.transaction.TransactionActivity;
import com.shabab.mezz.activity.utility.UtilityActivity;
import com.shabab.mezz.model.Mess;
import com.shabab.mezz.model.Transaction;
import com.shabab.mezz.model.User;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sp;
    User user;
    Mess mess;

    TextView messName;
    TextView messBalance;

    MaterialCardView mealBtn;
    MaterialCardView purchaseBtn;
    MaterialCardView utilityBtn;
    MaterialCardView transactionBtn;
    MaterialCardView billBtn;

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

        messName.setText(mess.getName());
        messBalance.setText("Balance: " + mess.getBalance());

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

    }
}