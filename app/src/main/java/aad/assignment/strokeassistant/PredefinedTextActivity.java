package aad.assignment.strokeassistant;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.List;

import aad.assignment.strokeassistant.model.PredefinedText;

public class PredefinedTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predefined_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DragListView listView = (DragListView) findViewById(R.id.list_predefined_text);
        listView.setLayoutManager(new LinearLayoutManager(this));

        //TODO clear all text, string

        List<PredefinedText> data = new ArrayList<>();
        data.add(new PredefinedText(1, "A"));
        data.add(new PredefinedText(2, "B"));
        data.add(new PredefinedText(3, "C"));
        data.add(new PredefinedText(4, "D"));
        data.add(new PredefinedText(5, "D"));
        data.add(new PredefinedText(6, "D"));
        data.add(new PredefinedText(7, "D"));
        data.add(new PredefinedText(8, "D"));
        data.add(new PredefinedText(9, "D"));
        data.add(new PredefinedText(10, "D"));
        data.add(new PredefinedText(11, "D"));
        data.add(new PredefinedText(12, "D"));
        data.add(new PredefinedText(13, "D"));
        data.add(new PredefinedText(14, "D"));
        data.add(new PredefinedText(15, "D"));

        PredefinedTextAdapter adapter = new PredefinedTextAdapter(data);
        listView.setAdapter(adapter, true);
        listView.setCanDragHorizontally(false);
        listView.setCustomDragItem(new MyDragItem(PredefinedTextActivity.this, R.layout.list_item_predefined_text));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.predefined_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) finish();
        else if (id == R.id.action_add_predefined_text) {
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyDragItem extends DragItem {
        private Context context;

        MyDragItem(Context context,
                   int layoutId) {
            super(context, layoutId);
            this.context = context;
        }

        @Override
        public void onBindDragView(View clickedView,
                                   View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.predefined_text)).getText();
            ((TextView) dragView.findViewById(R.id.predefined_text)).setText(text);
            ((TextView) dragView.findViewById(R.id.predefined_text)).setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_bright));
        }
    }
}
