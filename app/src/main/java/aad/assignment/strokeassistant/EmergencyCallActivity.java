package aad.assignment.strokeassistant;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Collections;

import aad.assignment.strokeassistant.model.EmergencyContact;

public class EmergencyCallActivity extends AppCompatActivity {

    private final Context context = EmergencyCallActivity.this;
    private ViewSwitcher viewSwitcher;

    private static final Integer FIRST_EMERGENCY_CONTACT = 0;
    private static final Integer SHOW_NEXT_VIEW = 1;

    private ArrayList<EmergencyContact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contacts = EmergencyContact.load(this);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.vs_emergency_contact);
        if (!contacts.isEmpty()) viewSwitcher.showNext();
    }

    @Override
    protected void onResume(){
        super.onResume();
        contacts = EmergencyContact.load(this);
        EmergencyCallAdapter adapter = new EmergencyCallAdapter(contacts, context, viewSwitcher);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.emergency_contacts_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) finish();
        else if (id == R.id.action_new_emergency_contact) newEmergencyContact();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.emergency_call, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FIRST_EMERGENCY_CONTACT) {
            if (resultCode == SHOW_NEXT_VIEW) {
                viewSwitcher.showNext();
            }
        }
    }

    private void newEmergencyContact() {
        Intent intent = new Intent(context, NewEmergencyContactActivity.class);
        startActivityForResult(intent, FIRST_EMERGENCY_CONTACT);
    }
}
