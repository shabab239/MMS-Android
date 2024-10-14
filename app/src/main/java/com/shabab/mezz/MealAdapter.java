package com.shabab.mezz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.model.User;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<User> userList;
    private Context context;

    public MealAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtUserName.setText(user.getName());

        // Handle up/down arrow button clicks
        holder.btnUp.setOnClickListener(v -> {
            int currentMeals = Integer.parseInt(holder.edtMealCount.getText().toString());
            holder.edtMealCount.setText(String.valueOf(currentMeals + 1));
        });

        holder.btnDown.setOnClickListener(v -> {
            int currentMeals = Integer.parseInt(holder.edtMealCount.getText().toString());
            if (currentMeals > 0) {
                holder.edtMealCount.setText(String.valueOf(currentMeals - 1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserName;
        EditText edtMealCount;
        Button btnUp, btnDown;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            edtMealCount = itemView.findViewById(R.id.edtMealCount);
            btnUp = itemView.findViewById(R.id.btnUp);
            btnDown = itemView.findViewById(R.id.btnDown);
        }
    }
}
