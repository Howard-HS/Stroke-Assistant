package aad.assignment.strokeassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPredefinedText = (Button) findViewById(R.id.btnPredefinedText);
        btnPredefinedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PredefinedTextActivity.class));
            }
        });


        Button btnMemGame = (Button) findViewById(R.id.btnToMemGame);
        btnMemGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MemGameActivity.class));
            }
        });
    }
}
