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
public class Purchase {

    @SerializedName("id")
    private Long id;

    @SerializedName("description")
    private String description;

    @SerializedName("cost")
    private Double cost;

    @SerializedName("date")
    private Date date;

    @SerializedName("user")
    private User user;

    public static List<Purchase> getPurchases(Map<String, Object> data, Context context) {
        try {
            if (data != null && data.containsKey("purchases")) {
                List<Purchase> purchaseList = new ArrayList<>();

                Gson gson = new Gson();
                String jsonString = gson.toJson(data.get("purchases"));
                JSONArray purchasesArray = new JSONArray(jsonString);

                for (int i = 0; i < purchasesArray.length(); i++) {
                    JSONObject purchaseObject = purchasesArray.getJSONObject(i);
                    Purchase purchase = gson.fromJson(purchaseObject.toString(), Purchase.class);
                    purchaseList.add(purchase);
                }
                return purchaseList;
            }
        } catch (JSONException e) {
            Toast.makeText(context, "Error parsing purchase data", Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<>();
    }
}
