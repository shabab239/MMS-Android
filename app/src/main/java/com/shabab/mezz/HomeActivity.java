package com.shabab.mezz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.shabab.mezz.model.Mess;
import com.shabab.mezz.model.User;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sp;
    User user;
    Mess mess;

    TextView messName;
    TextView messBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sp = getSharedPreferences("sp", MODE_PRIVATE);

        user = new Gson().fromJson(sp.getString("user", null), User.class);
        mess = new Gson().fromJson(sp.getString("mess", null), Mess.class);

        messName = findViewById(R.id.messName);
        messBalance = findViewById(R.id.messBalance);

        messName.setText(mess.getName());
        messBalance.setText("Balance: " + mess.getBalance());
    }
}