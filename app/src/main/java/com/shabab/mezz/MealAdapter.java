package com.shabab.mezz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
        holder.userName.setText(user.getName());

        holder.mealUp.setOnClickListener(v -> {
            int currentMeals = Integer.parseInt(holder.mealCount.getText().toString());
            holder.mealCount.setText(String.valueOf(currentMeals + 1));
        });

        holder.mealDown.setOnClickListener(v -> {
            int currentMeals = Integer.parseInt(holder.mealCount.getText().toString());
            if (currentMeals > 0) {
                holder.mealCount.setText(String.valueOf(currentMeals - 1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView mealCount;
        ImageButton mealUp, mealDown;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            mealCount = itemView.findViewById(R.id.mealCount);
            mealUp = itemView.findViewById(R.id.mealUp);
            mealDown = itemView.findViewById(R.id.mealDown);
        }
    }
}
