package aad.assignment.strokeassistant.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by chooh on 1/29/2018.
 */

public class HealthRecord {
    private int id, bloodPressure, bloodSugar;
    private float bodyWeight, bodyHeight, bmi;
    private Date date;
    private final static String HEALTH_REC_KEY = "HEALTH_REC";

    public HealthRecord(int bp, int bs, float bw, float bh) {
        bloodPressure = bp;
        bloodSugar = bs;
        bodyWeight = bw;
        bodyHeight = bh;

        date = Calendar.getInstance().getTime();
        setBmi();
    }

    public static ArrayList<HealthRecord> load(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String               data        = preferences.getString(HEALTH_REC_KEY, "");
        Type type        = new TypeToken<ArrayList<HealthRecord>>() {}.getType();
        ArrayList<HealthRecord> list        = new Gson().fromJson(data, type);

        list = list != null ? list : new ArrayList<>();

        return list;
    }

    public static void save(Context context,
                            ArrayList<HealthRecord> data) {
        SharedPreferences        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor      = preferences.edit();

        editor.putString(HEALTH_REC_KEY, new Gson().toJson(data));
        editor.apply();
    }

    private void setBmi() { bmi = bodyWeight / (bodyHeight * bodyHeight); }

    public int getId() { return id; }

    public int getBloodPressure() { return bloodPressure; }

    public int getBloodSugar() { return bloodSugar; }

    public float getBodyWeight() { return bodyWeight; }

    public float getBodyHeight() { return bodyHeight; }

    public Date getDate() { return date; }

    public float getBmi() { return bmi; }

}
