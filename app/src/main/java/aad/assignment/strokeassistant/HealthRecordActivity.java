package aad.assignment.strokeassistant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import aad.assignment.strokeassistant.model.HealthRecord;

public class HealthRecordActivity extends AppCompatActivity {

    private ArrayList<HealthRecord> records;
    private Context context = HealthRecordActivity.this;
    private ViewSwitcher viewSwitcher;
    private int SHOW_NEXT_VIEW = 1,
            FIRST_HEALTH_RECORD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        records = HealthRecord.load(this);

        setContentView(R.layout.activity_health_record);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.vs_health_records);

        if(!records.isEmpty()) viewSwitcher.showNext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onResume(){
        super.onResume();
        records = HealthRecord.load(this);
        HealthRecordAdapter adapter = new HealthRecordAdapter(records, context, viewSwitcher);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.health_cards);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.health_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) finish();
        else if (id == R.id.action_add_health_record) addHealthRecord();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FIRST_HEALTH_RECORD && resultCode == SHOW_NEXT_VIEW) {
            viewSwitcher.showNext();
        }
    }

    private void addHealthRecord() {
        startActivityForResult(new Intent(HealthRecordActivity.this, AddHealthRecord.class), FIRST_HEALTH_RECORD);
    }
}
