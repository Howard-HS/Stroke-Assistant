package aad.assignment.strokeassistant;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import aad.assignment.strokeassistant.model.EmergencyContact;

/**
 * Created by howard on 2/5/18.
 */

public class EmergencyCallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<EmergencyContact> contacts;
    private Context context;
    private ViewSwitcher viewSwitcher;

    private Integer PERMISSION_TO_CALL_PHONE;

    EmergencyCallAdapter(ArrayList<EmergencyContact> contacts, Context context, ViewSwitcher viewSwitcher) {
        this.contacts = contacts;
        this.context = context;
        this.viewSwitcher = viewSwitcher;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView contactPersonDisplay;
        ImageView deleteContact;

        ItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.emergency_contact_cv);
            contactPersonDisplay = (TextView) itemView.findViewById(R.id.emergency_contact_name);
            deleteContact = (ImageView) itemView.findViewById(R.id.btn_delete_emergency_contact);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_contact_cards, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder emergencyContactViewHolder, int index) {
        if (emergencyContactViewHolder instanceof ItemViewHolder) {
            EmergencyContact contact = contacts.get(index);
            String contactPerson = contact.getContactPerson();
            ItemViewHolder holder = (ItemViewHolder) emergencyContactViewHolder;

            if (contact.getActiveEmergencyContact()) {
                holder.contactPersonDisplay.setText(contactPerson.concat(" (Default)"));
                holder.contactPersonDisplay.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
            } else {
                holder.contactPersonDisplay.setText(contactPerson);
                holder.contactPersonDisplay.setTextColor(context.getResources().getColor(R.color.colorText));
            }

            holder.contactPersonDisplay.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View emergencyContactView = inflater.inflate(R.layout.emergency_contact_details, null, false);

                TextView displayContactPerson = (TextView) emergencyContactView.findViewById(R.id.display_contact_person);
                TextView displayContactNumber = (TextView) emergencyContactView.findViewById(R.id.display_contact_number);
                TextView displayContactAddress = (TextView) emergencyContactView.findViewById(R.id.display_contact_address);

                displayContactPerson.setText(contact.getContactPerson());
                displayContactNumber.setText(contact.getContactNumber());
                displayContactAddress.setText(contact.getContactAddress());

                builder
                        .setCancelable(true)
                        .setTitle("Contact Details")
                        .setView(emergencyContactView)
                        .setPositiveButton("Call", (dialogInterface, i) -> {
//                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                                ActivityCompat.requestPermissions(context.., new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_TO_CALL_PHONE);
//                            }
                            context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + contact.getContactNumber())));
                        })
                        .setNegativeButton("Set Default", (dialogInterface, i) -> {
                            for (EmergencyContact emergencyContact : contacts) {
                                emergencyContact.setActiveEmergencyContact(false);
                            }
                            contact.setActiveEmergencyContact(true);
                            Collections.sort(contacts, (emergencyContact, t1) -> Boolean.compare(t1.getActiveEmergencyContact(), emergencyContact.getActiveEmergencyContact()));
                            notifyDataSetChanged();
                        });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(true);
                dialog.show();
                if (contact.getActiveEmergencyContact()){
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                }
            });

            holder.deleteContact.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete this contact?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            contacts.remove(index);
                            EmergencyContact.save(context, contacts);
                            notifyDataSetChanged();
                            if (contacts.isEmpty()) viewSwitcher.showNext();
                        })
                        .create().show();
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() { return contacts.size();}
}
