package com.shabab.mezz.model;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Utility {

    @SerializedName("id")
    private Long id;

    @SerializedName("month")
    private Integer month;

    @SerializedName("year")
    private Integer year;

    @SerializedName("cost")
    private Double cost;

    public String getMonthName() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, this.month - 1);
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    public static List<Utility> getUtilities(Map<String, Object> data, Context context) {
        List<Utility> utilityList = new ArrayList<>();
        try {
            if (data != null && data.containsKey("utilities")) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(data.get("utilities"));
                JSONArray utilitiesArray = new JSONArray(jsonString);

                for (int i = 0; i < utilitiesArray.length(); i++) {
                    JSONObject utilityObject = utilitiesArray.getJSONObject(i);
                    Utility utility = gson.fromJson(utilityObject.toString(), Utility.class);
                    utilityList.add(utility);
                }
            }
        } catch (JSONException e) {
            Toasty.error(context, "Error parsing utility data", Toast.LENGTH_SHORT).show();
        }
        return utilityList;
    }
}
