package com.shabab.mezz.model;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {

    @SerializedName("id")
    private Long id;

    @SerializedName("date")
    private Date date;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("user")
    private User user;

    @SerializedName("type")
    private TransactionType type;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }

    public static List<Transaction> getTransactions(Map<String, Object> data, Context context) {
        try {
            if (data != null && data.containsKey("transactions")) {
                List<Transaction> transactionList = new ArrayList<>();

                Gson gson = new Gson();
                String jsonString = gson.toJson(data.get("transactions"));
                JSONArray transactionsArray = new JSONArray(jsonString);

                for (int i = 0; i < transactionsArray.length(); i++) {
                    JSONObject transactionObject = transactionsArray.getJSONObject(i);
                    Transaction transaction = gson.fromJson(transactionObject.toString(), Transaction.class);
                    transactionList.add(transaction);
                }
                return transactionList;
            }
        } catch (JSONException e) {
            Toast.makeText(context, "Error parsing transaction data", Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<>();
    }
}
