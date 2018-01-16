package aad.assignment.strokeassistant.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by JohnSiah on 17/12/2017.
 */

public class PredefinedText implements Comparable<PredefinedText> {
    private int id;
    private String message;
    private boolean isSelected;
    private final static String PERF_KEY = "PERF_PREDEFINED_TEXT";

    public PredefinedText(int id,
                          String message) {
        this.id = id;
        this.message = message;
        this.isSelected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static int getNextUniqueID(List<PredefinedText> data) {
        int                  counter = 0;
        List<PredefinedText> temp    = new ArrayList<>(data);

        Collections.sort(temp);

        for (PredefinedText text : temp) {
            if (text.getId() != counter) return counter;
            else counter++;
        }

        return counter;
    }

    public static List<PredefinedText> load(Context context) {
        SharedPreferences    preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String               data        = preferences.getString(PERF_KEY, "");
        Type                 type        = new TypeToken<List<PredefinedText>>() {}.getType();
        List<PredefinedText> list        = new Gson().fromJson(data, type);

        list = list != null ? list : new ArrayList<>();

        for (PredefinedText obj : list) obj.setSelected(false);

        return list;
    }

    public static void save(Context context,
                            List<PredefinedText> data) {
        SharedPreferences        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor      = preferences.edit();

        editor.putString(PERF_KEY, new Gson().toJson(data));
        editor.apply();
    }

    @Override
    public int compareTo(@NonNull PredefinedText other) {
        return id < other.getId() ? -1 : 1;
    }
}
