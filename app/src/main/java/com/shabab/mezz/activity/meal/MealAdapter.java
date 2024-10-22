package com.shabab.mezz.activity.meal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.R;
import com.shabab.mezz.dto.MealDTO;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<MealDTO> mealList;
    private Context context;

    public MealAdapter(List<MealDTO> mealList, Context context) {
        this.mealList = mealList;
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
        MealDTO mealDTO = mealList.get(position);
        holder.userName.setText(mealDTO.getUsername());
        holder.meals.setText(String.valueOf(mealDTO.getMeals()));

        holder.mealUp.setOnClickListener(v -> {
            double currentMeals = Double.parseDouble(holder.meals.getText().toString());
            holder.meals.setText(String.valueOf(currentMeals + 1));
            mealDTO.setMeals(currentMeals + 1);
        });

        holder.mealDown.setOnClickListener(v -> {
            double currentMeals = Double.parseDouble(holder.meals.getText().toString());
            if (currentMeals > 0) {
                holder.meals.setText(String.valueOf(currentMeals - 1));
                mealDTO.setMeals(currentMeals - 1);
            }
        });

        holder.mealUp.setOnLongClickListener(v -> {
            double currentMeals = Double.parseDouble(holder.meals.getText().toString());
            holder.meals.setText(String.valueOf(currentMeals + 0.5));
            mealDTO.setMeals(currentMeals + 0.5);
            return true; // Indicate that the long press was handled
        });

        holder.mealDown.setOnLongClickListener(v -> {
            double currentMeals = Double.parseDouble(holder.meals.getText().toString());
            if (currentMeals >= 0.5) {
                holder.meals.setText(String.valueOf(currentMeals - 0.5));
                mealDTO.setMeals(currentMeals - 0.5);
            }
            return true; // Indicate that the long press was handled
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public List<MealDTO> getMealList() {
        return mealList;
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
