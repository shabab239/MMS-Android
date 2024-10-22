package com.shabab.mezz.activity.bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.R;
import com.shabab.mezz.model.Bill;

import java.text.SimpleDateFormat;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private List<Bill> billList;
    private Context context;

    public BillAdapter(List<Bill> billList, Context context) {
        this.billList = billList;
        this.context = context;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);

        String monthYear = getMonthName(bill.getMonth()) + " " + bill.getYear();
        holder.month_year.setText(monthYear);

        holder.username.setText(bill.getUser().getName());

        holder.mealCost.setText("Meal Cost: " + String.format("%.2f", bill.getMealCost()) + " TK.");
        holder.utilityCost.setText("Utility Cost: " + String.format("%.2f", bill.getUtilityCost()) + " TK.");
        holder.finalAmount.setText("Final Amount: " + String.format("%.2f", bill.getFinalAmount()) + " TK.");
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView month_year;
        TextView username;
        TextView mealCost;
        TextView utilityCost;
        TextView finalAmount;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            month_year = itemView.findViewById(R.id.month_year);
            username = itemView.findViewById(R.id.username);
            mealCost = itemView.findViewById(R.id.mealCost);
            utilityCost = itemView.findViewById(R.id.utilityCost);
            finalAmount = itemView.findViewById(R.id.finalAmount);
        }
    }

    private String getMonthName(int month) {
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return months[month - 1];
    }
}
