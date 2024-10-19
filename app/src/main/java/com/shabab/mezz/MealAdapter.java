package com.shabab.mezz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.model.MealRequest;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<MealRequest> mealRequestList;
    private Context context;

    public MealAdapter(List<MealRequest> mealRequestList, Context context) {
        this.mealRequestList = mealRequestList;
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
        MealRequest mealRequest = mealRequestList.get(position);
        holder.userName.setText(mealRequest.getUsername());
        holder.meals.setText(String.valueOf(mealRequest.getMeals()));

        holder.mealUp.setOnClickListener(v -> {
            int currentMeals = Integer.parseInt(holder.meals.getText().toString());
            holder.meals.setText(String.valueOf(currentMeals + 1));
            mealRequest.setMeals(currentMeals + 1);
        });

        holder.mealDown.setOnClickListener(v -> {
            int currentMeals = Integer.parseInt(holder.meals.getText().toString());
            if (currentMeals > 0) {
                holder.meals.setText(String.valueOf(currentMeals - 1));
                mealRequest.setMeals(currentMeals - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealRequestList.size();
    }

    public List<MealRequest> getMealRequestList() {
        return mealRequestList;
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView meals;
        ImageButton mealUp, mealDown;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            meals = itemView.findViewById(R.id.meals);
            mealUp = itemView.findViewById(R.id.mealUp);
            mealDown = itemView.findViewById(R.id.mealDown);
        }
    }
}
