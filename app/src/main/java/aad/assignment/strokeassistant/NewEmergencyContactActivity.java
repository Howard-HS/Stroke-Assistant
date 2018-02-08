package aad.assignment.strokeassistant;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;

import aad.assignment.strokeassistant.model.EmergencyContact;

public class NewEmergencyContactActivity extends AppCompatActivity {

    public EmergencyContactValidator emergencyContactValidator = new EmergencyContactValidator();
    private final Context context = NewEmergencyContactActivity.this;

    private static final Integer SHOW_NEXT_VIEW = 1;

    private ArrayList<EmergencyContact> contacts;

    private enum FieldError {
        MISSING_CONTACT_PERSON,
        MISSING_CONTACT_NUMBER,
        MISSING_CONTACT_ADDRESS,
        INVALID_NUMBER,
        NUM_PARSE_EXCEPTION
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_emergency_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contacts = EmergencyContact.load(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_emergency_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) finish();
        else if (id == R.id.action_save_emergency_contact) addNewEmergencyContact();
        return super.onOptionsItemSelected(item);
    }

    private void addNewEmergencyContact() {
        EditText fieldContactPerson = (EditText) findViewById(R.id.field_contact_person);
        EditText fieldContactNumber = (EditText) findViewById(R.id.field_contact_number);
        EditText fieldContactAddress = (EditText) findViewById(R.id.field_contact_address);

        String contactPerson = fieldContactPerson.getText().toString();
        String contactNumber = fieldContactNumber.getText().toString();
        String contactAddress = fieldContactAddress.getText().toString();

        if (contactPerson.isEmpty()) {
            displayErrMsg(fieldContactPerson, FieldError.MISSING_CONTACT_PERSON);
            return;
        }
        if (contactNumber.isEmpty()) {
            displayErrMsg(fieldContactNumber, FieldError.MISSING_CONTACT_NUMBER);
            return;
        }

        String validatedContactNumber = emergencyContactValidator.validateContactNumber(contactNumber);

        if (validatedContactNumber.equals("INVALID_NUMBER")) {
            displayErrMsg(fieldContactNumber, FieldError.INVALID_NUMBER);
        } else if (validatedContactNumber.equals("NUM_PARSE_EXCEPTION")) {
            displayErrMsg(fieldContactNumber, FieldError.NUM_PARSE_EXCEPTION);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder
                    .setCancelable(false)
                    .setTitle("Default Emergency Contact?")
                    .setMessage("Do you want to set this contact as your default emergency contact? You still can do this later.")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        if (contacts.isEmpty()) {
                            EmergencyContact newContact = new EmergencyContact(contactPerson, validatedContactNumber, contactAddress, true);
                            contacts.add(newContact);
                            EmergencyContact.save(this, contacts);
                            setResult(SHOW_NEXT_VIEW);
                            dialogInterface.dismiss();
                            finish();
                        } else {
                            EmergencyContact newContact = new EmergencyContact(contactPerson, validatedContactNumber, contactAddress, true);
                            for (EmergencyContact contact : contacts) {
                                contact.setActiveEmergencyContact(false);
                            }
                            contacts.add(newContact);
                            Collections.sort(contacts, (emergencyContact, t1) -> Boolean.compare(t1.getActiveEmergencyContact(), emergencyContact.getActiveEmergencyContact()));
                            EmergencyContact.save(this, contacts);
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                        EmergencyContact emergencyContact = new EmergencyContact(contactPerson, validatedContactNumber, contactAddress, false);
                        contacts.add(emergencyContact);
                        EmergencyContact.save(this, contacts);
                        if (contacts.size() == 1) setResult(SHOW_NEXT_VIEW);
                        dialogInterface.dismiss();
                        finish();
                    })
                    .create().show();
        }
    }

    private void displayErrMsg(EditText editText, FieldError fieldError) {
        editText.setText("");
        editText.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));

        switch (fieldError) {
            case MISSING_CONTACT_PERSON:
                editText.setHint("Contact person required!");
                break;
            case MISSING_CONTACT_NUMBER:
                editText.setHint("Contact number required!");
                break;
            case MISSING_CONTACT_ADDRESS:
                break;
            case INVALID_NUMBER:
                editText.setHint("Invalid contact number!");
                break;
            case NUM_PARSE_EXCEPTION:
                editText.setHint("Unexpected error! Please retype again!");
                break;
        }
    }
}
