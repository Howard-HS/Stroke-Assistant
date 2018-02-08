package aad.assignment.strokeassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView btnPredefinedText = (CardView) findViewById(R.id.btnPredefinedText);
        btnPredefinedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PredefinedTextActivity.class));
            }
        });

        CardView btnMemGame = (CardView) findViewById(R.id.btnToMemGame);
        btnMemGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MemGameActivity.class));
            }
        });

        CardView btnHealthRec = (CardView) findViewById(R.id.btnHealthRec);
        btnHealthRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HealthRecordActivity.class));
            }
        });

        CardView btnReminder = (CardView) findViewById(R.id.btnReminder);
        btnReminder.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ReminderActivity.class));
        });

        CardView btnEmergencyCall = (CardView) findViewById(R.id.btnEmergencyCall);
        btnEmergencyCall.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, EmergencyCallActivity.class));
        });
    }
}
