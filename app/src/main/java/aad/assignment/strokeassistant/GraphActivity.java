package aad.assignment.strokeassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import aad.assignment.strokeassistant.model.HealthRecord;

public class GraphActivity extends AppCompatActivity {
    private final int BLOOD_PRESSURE_GRAPH = 0,
            BLOOD_SUGAR_GRAPH = 1,
            BMI_GRAPH = 2;
    private final String GRAPH_TYPE = "GRAPH_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_graph);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series;

        int type = getIntent().getExtras().getInt(GRAPH_TYPE);

        ArrayList<HealthRecord> records = HealthRecord.load(this);

        DataPoint[] dataPoint = new DataPoint[records.size()];

        if (type == BLOOD_PRESSURE_GRAPH) {
            int index = 0;
            for (HealthRecord record : records) {
                dataPoint[index] = new DataPoint(index, record.getBloodPressure());
                index ++;
            }
        } else if (type == BLOOD_SUGAR_GRAPH) {
            int index = 0;
            for (HealthRecord record : records) {
                dataPoint[index] = new DataPoint(index, record.getBloodSugar());
                index ++;
            }
        } else {
            int index = 0;
            for (HealthRecord record : records) {
                dataPoint[index] = new DataPoint(index, record.getBmi());
                index ++;
            }
        }


        series = new LineGraphSeries<>(dataPoint);

        graph.addSeries(series);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
