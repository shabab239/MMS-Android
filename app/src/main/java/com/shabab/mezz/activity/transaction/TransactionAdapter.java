package com.shabab.mezz.activity.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.R;
import com.shabab.mezz.model.Transaction;
import com.shabab.mezz.model.Utility;

import java.text.SimpleDateFormat;
import java.util.List;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;
    private Context context;

    public TransactionAdapter(List<Transaction> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.amount.setText(transaction.getAmount() + " TK.");
        holder.date.setText(new SimpleDateFormat("yyyy-MM-dd").format(transaction.getDate()));
        holder.username.setText(transaction.getUser().getName());
        holder.type.setText(transaction.getType().name());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView amount;
        TextView date;
        TextView type;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            type = itemView.findViewById(R.id.type);
        }
    }
}

