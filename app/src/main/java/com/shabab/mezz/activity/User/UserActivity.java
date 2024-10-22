package com.shabab.mezz.activity.User;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.R;
import com.shabab.mezz.activity.transaction.TransactionActivity;
import com.shabab.mezz.activity.transaction.TransactionAdapter;
import com.shabab.mezz.api.ApiResponse;
import com.shabab.mezz.api.service.ApiService;
import com.shabab.mezz.api.service.UserService;
import com.shabab.mezz.model.User;
import com.shabab.mezz.util.Wait;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private Button addUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(new ArrayList<>(), UserActivity.this);
        recyclerView.setAdapter(userAdapter);

        loadUsers();
    }

    private void loadUsers() {
        Wait.show(this);
        UserService userService = ApiService.getService(UserService.class);

        Call<ApiResponse> call = userService.getAllUsers();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                try {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
                        userList = User.getUsers(apiResponse.getData(), UserActivity.this);

                        userAdapter = new UserAdapter(userList, UserActivity.this);
                        recyclerView.setAdapter(userAdapter);
                    } else {
                        Toasty.error(UserActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.error(UserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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