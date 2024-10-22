package com.shabab.mezz.activity.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabab.mezz.R;
import com.shabab.mezz.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.name.setText(user.getName());
        holder.balance.setText("Balance: " + user.getBalance() + " TK.");
        holder.cell.setText("Cell: " + user.getCell());
        holder.email.setText("Email: " + user.getEmail());
        holder.role.setText("Role: " + user.getRole().name().split("_")[1]);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView balance;
        TextView cell;
        TextView email;
        TextView role;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            balance = itemView.findViewById(R.id.balance);
            cell = itemView.findViewById(R.id.cell);
            email = itemView.findViewById(R.id.email);
            role = itemView.findViewById(R.id.role);
        }
    }

}
