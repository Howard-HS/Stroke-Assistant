package aad.assignment.strokeassistant;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import aad.assignment.strokeassistant.model.HealthRecord;

public class AddHealthRecord extends AppCompatActivity {

    private final static int MIN_BLOOD_PRESSURE = 0,
            MAX_BLOOD_PRESSURE = 250,
            MIN_BLOOD_SUGAR = 0,
            MAX_BLOOD_SUGAR = 200,
            MIN_WEIGHT = 0,
            MIN_HEIGHT = 0,
            BLOOD_PRESSURE_ERROR = 0,
            BLOOD_SUGAR_ERROR = 1,
            WEIGHT_HEIGHT_ERROR = 2,
            SHOW_NEXT_VIEW = 1;
    private Context context = AddHealthRecord.this;
    private ArrayList<HealthRecord> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        records = HealthRecord.load(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_health_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) finish();
        else if (id == R.id.action_save_health_record) saveHealthRecord();

        return super.onOptionsItemSelected(item);
    }

    private void saveHealthRecord() {
        EditText bloodPressure = (EditText) findViewById(R.id.add_blood_pressure);
        EditText bloodSugar = (EditText) findViewById(R.id.add_blood_sugar);
        EditText bodyHeight = (EditText) findViewById(R.id.add_body_height);
        EditText bodyWeight = (EditText) findViewById(R.id.add_body_weight);

        String bPressure = bloodPressure.getText().toString().trim();
        String bSugar = bloodSugar.getText().toString().trim();
        String bHeight = bodyHeight.getText().toString().trim();
        String bWeight = bodyWeight.getText().toString().trim();

        if (bPressure.isEmpty() || bSugar.isEmpty() || bHeight.isEmpty() || bWeight.isEmpty()) {
            Toast.makeText(context, R.string.empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        int bPressureValue, bSugarValue;
        float bWeightValue, bHeightValue;

        bPressureValue = Integer.parseInt(bPressure);
        bSugarValue = Integer.parseInt(bSugar);
        bWeightValue = Float.parseFloat(bWeight);
        bHeightValue = Float.parseFloat(bHeight);
        if (bWeightValue < MIN_WEIGHT)
            invalidFieldHint(bodyHeight, WEIGHT_HEIGHT_ERROR);
        else if (bHeightValue < MIN_HEIGHT)
            invalidFieldHint(bodyWeight, WEIGHT_HEIGHT_ERROR);
        else if (bSugarValue < MIN_BLOOD_SUGAR || bSugarValue > MAX_BLOOD_SUGAR)
            invalidFieldHint(bloodSugar, BLOOD_SUGAR_ERROR);
        else if (bPressureValue < MIN_BLOOD_PRESSURE || bPressureValue > MAX_BLOOD_PRESSURE)
            invalidFieldHint(bloodPressure, BLOOD_PRESSURE_ERROR);
        else {
            HealthRecord newRecord = new HealthRecord(bPressureValue, bSugarValue, bWeightValue, bHeightValue);
            records.add(newRecord);
            HealthRecord.save(this, records);
            Toast.makeText(context, R.string.health_rec_saved, Toast.LENGTH_SHORT).show();
            if (records.size() == 1) setResult(SHOW_NEXT_VIEW);
            finish();
        }
    }

    private void invalidFieldHint(EditText editText,
                                  int err) {
        editText.setText("");

        if (err == BLOOD_PRESSURE_ERROR) editText.setHint(R.string.invalid_blood_pressure);
        else if (err == BLOOD_SUGAR_ERROR) editText.setHint(R.string.invalid_blood_sugar);
        else editText.setHint(R.string.invalid_weight_height);

        editText.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
    }
}
