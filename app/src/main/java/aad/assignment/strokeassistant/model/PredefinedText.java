package aad.assignment.strokeassistant.model;

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

    public PredefinedText(int id,
                          String message) {
        this.id = id;
        this.message = message;
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

    public static String toJson(List<PredefinedText> data) {
        return new Gson().toJson(data);
    }

    public static List<PredefinedText> fromJson(String data) {
        Type type = new TypeToken<List<PredefinedText>>() {}.getType();
        return new Gson().fromJson(data, type);
    }

    @Override
    public int compareTo(@NonNull PredefinedText other) {
        return id < other.getId() ? -1 : 1;
    }
}
