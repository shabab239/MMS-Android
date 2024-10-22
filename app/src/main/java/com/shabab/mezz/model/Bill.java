package com.shabab.mezz.model;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bill {

    @SerializedName("id")
    private Long id;

    @SerializedName("month")
    private Integer month;

    @SerializedName("year")
    private Integer year;

    @SerializedName("mealCost")
    private Double mealCost;

    @SerializedName("utilityCost")
    private Double utilityCost;

    @SerializedName("finalAmount")
    private Double finalAmount;

    @SerializedName("user")
    private User user;

    public static List<Bill> getBills(Map<String, Object> data, Context context) {
        List<Bill> billList = new ArrayList<>();
        if (data != null && data.containsKey("bills")) {
            try {
                Gson gson = new Gson();
                String jsonString = gson.toJson(data.get("bills"));
                JSONArray billsArray = new JSONArray(jsonString);

                for (int i = 0; i < billsArray.length(); i++) {
                    JSONObject billObject = billsArray.getJSONObject(i);
                    Bill bill = gson.fromJson(billObject.toString(), Bill.class);
                    billList.add(bill);
                }
            } catch (JSONException e) {
                Toast.makeText(context, "Error parsing purchase data", Toast.LENGTH_SHORT).show();
            }
        }
        return billList;
    }
}

