package aad.assignment.strokeassistant.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by howard on 2/5/18.
 */

public class EmergencyContact {

    private String contactPerson, contactNumber, contactAddress;
    private Boolean activeEmergencyContact;
    private final static String NOT_PROVIDED = "Field not provided";
    private static final String PERF_EMER_KEY = "PERF_EMERGENCY_CONTACT";

    public EmergencyContact(String contactPerson, String contactNumber, String contactAddress, Boolean activeEmergencyContact) {
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.contactAddress = contactAddress;
        this.activeEmergencyContact = activeEmergencyContact;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactAddress() {
        if (contactAddress.isEmpty()) {
            return NOT_PROVIDED;
        }
        return contactAddress;
    }

    public void setActiveEmergencyContact(Boolean isActiveEmergencyContact) {
        this.activeEmergencyContact = isActiveEmergencyContact;
    }

    public Boolean getActiveEmergencyContact() {
       return activeEmergencyContact;
    }

    public static ArrayList<EmergencyContact> load(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String data = preferences.getString(PERF_EMER_KEY, "");
        Type type = new TypeToken<ArrayList<EmergencyContact>>() {}.getType();
        ArrayList<EmergencyContact> list = new Gson().fromJson(data, type);

        list = list != null ? list : new ArrayList<>();

        return list;
    }

    public static void save(Context context, ArrayList<EmergencyContact> contacts) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(PERF_EMER_KEY, new Gson().toJson(contacts));
        editor.apply();
    }

}
