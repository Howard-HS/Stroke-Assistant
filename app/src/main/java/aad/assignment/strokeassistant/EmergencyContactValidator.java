package aad.assignment.strokeassistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by howard on 2/2/18.
 */

public class EmergencyContactValidator {
    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String INVALID_NUMBER = "INVALID_NUMBER";
    private static final String NUM_PARSE_EXCEPTION = "NUM_PARSE_EXCEPTION";

    public String validateContactNumber(String contactNumber) {
        try {
            Phonenumber.PhoneNumber phoneNumberProto = phoneNumberUtil.parse(contactNumber, "MY");
            if (phoneNumberUtil.isValidNumber(phoneNumberProto)) {
                return phoneNumberUtil.format(phoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            } else {
                return INVALID_NUMBER;
            }
        } catch (NumberParseException e) {
            Log.e(NUM_PARSE_EXCEPTION, e.toString());
            return NUM_PARSE_EXCEPTION;
        }
    }
}
