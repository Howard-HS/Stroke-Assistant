package aad.assignment.strokeassistant.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by chooh on 2/2/2018.
 */

public class Reminder {
    private String title,
            description;
    private int hour,
            minute,
            id;
    private final static String REMINDER_KEY = "REMINDER";

    public Reminder(String title,
                    String description,
                    int hour,
                    int minute) {
        this.title = title;
        this.description = description;
        this.hour = hour;
        this.minute = minute;
        id = (int) System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getId() {
        return id;
    }

    public static ArrayList<Reminder> load(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String            data        = preferences.getString(REMINDER_KEY, "");
        Type type = new TypeToken<ArrayList<Reminder>>() {
        }.getType();
        ArrayList<Reminder> list = new Gson().fromJson(data, type);

        list = list != null ? list : new ArrayList<>();

        return list;
    }

    public static void save(Context context,
                            ArrayList<Reminder> data) {
        SharedPreferences        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor      = preferences.edit();

        editor.putString(REMINDER_KEY, new Gson().toJson(data));
        editor.apply();
    }
}
