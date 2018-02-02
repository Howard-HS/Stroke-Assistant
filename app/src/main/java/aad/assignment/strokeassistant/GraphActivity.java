package aad.assignment.strokeassistant;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import aad.assignment.strokeassistant.model.HealthRecord;

public class GraphActivity extends AppCompatActivity {
    private static final int BLOOD_PRESSURE_GRAPH = 0,
            BLOOD_SUGAR_GRAPH = 1;
    private static final String GRAPH_TYPE = "GRAPH_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_graph);

        GraphView                  graph     = (GraphView) findViewById(R.id.graph);
        ArrayList<HealthRecord>    records   = HealthRecord.load(this);
        int                        type      = getIntent().getExtras().getInt(GRAPH_TYPE);
        DataPoint[]                dataPoint = new DataPoint[records.size()];
        LineGraphSeries<DataPoint> series;

        if (type == BLOOD_PRESSURE_GRAPH) {
            int index = 0;
            for (HealthRecord record : records) {
                dataPoint[index] = new DataPoint(index, record.getBloodPressure());
                index++;
            }
            getSupportActionBar().setTitle(R.string.button_blood_pressure);
        } else if (type == BLOOD_SUGAR_GRAPH) {
            int index = 0;
            for (HealthRecord record : records) {
                dataPoint[index] = new DataPoint(index, record.getBloodSugar());
                index++;
            }
            getSupportActionBar().setTitle(R.string.button_blood_sugar);
        } else {
            int index = 0;
            for (HealthRecord record : records) {
                dataPoint[index] = new DataPoint(index, record.getBmi());
                index++;
            }
            getSupportActionBar().setTitle(R.string.button_bmi);
        }

        series = new LineGraphSeries<>(dataPoint);
        series.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        graph.addSeries(series);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
