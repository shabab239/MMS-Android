package com.shabab.mezz.activity.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.R;
import com.shabab.mezz.model.Utility;

import java.util.List;

public class UtilityAdapter extends RecyclerView.Adapter<UtilityAdapter.UtilityViewHolder> {

    private List<Utility> utilityList;
    private Context context;

    public UtilityAdapter(List<Utility> utilityList, Context context) {
        this.utilityList = utilityList;
        this.context = context;
    }

    @NonNull
    @Override
    public UtilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_utility, parent, false);
        return new UtilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UtilityViewHolder holder, int position) {
        Utility utility = utilityList.get(position);
        holder.month.setText("Month: " + utility.getMonthName());
        holder.year.setText("Year: " + utility.getYear());
        holder.cost.setText("Cost: " + utility.getCost() + " TK.");
    }

    @Override
    public int getItemCount() {
        return utilityList.size();
    }

    public static class UtilityViewHolder extends RecyclerView.ViewHolder {

        TextView month;
        TextView year;
        TextView cost;

        public UtilityViewHolder(@NonNull View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month);
            year = itemView.findViewById(R.id.year);
            cost = itemView.findViewById(R.id.cost);
        }
    }
}

