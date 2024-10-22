package com.shabab.mezz.activity.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.R;
import com.shabab.mezz.model.Purchase;

import java.text.SimpleDateFormat;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.MealViewHolder> {

    private List<Purchase> purchaseList;
    private Context context;

    public PurchaseAdapter(List<Purchase> purchaseList, Context context) {
        this.purchaseList = purchaseList;
        this.context = context;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Purchase purchase = purchaseList.get(position);
        holder.username.setText(purchase.getUser().getName());
        holder.description.setText(purchase.getDescription());
        holder.date.setText(new SimpleDateFormat("yyyy-MM-dd").format(purchase.getDate()));
        holder.cost.setText(purchase.getCost() + " TK.");
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public List<Purchase> getMealList() {
        return purchaseList;
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView description;
        TextView cost;
        TextView date;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
            cost = itemView.findViewById(R.id.cost);
            date = itemView.findViewById(R.id.date);
        }
    }
}
