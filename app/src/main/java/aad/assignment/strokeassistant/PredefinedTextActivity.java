package aad.assignment.strokeassistant;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import aad.assignment.strokeassistant.model.PredefinedText;

public class PredefinedTextActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private PredefinedTextAdapter adapter;
    private DragListView listView;
    private static final int INSERT_POS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predefined_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final float dp      = getResources().getDisplayMetrics().density;
        final int   PADDING = (int) (8 * dp);

        listView = (DragListView) findViewById(R.id.list_predefined_text);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.getRecyclerView().setPadding(PADDING, PADDING, PADDING, PADDING);
        listView.getRecyclerView().setClipToPadding(false);

        //TODO clear all text, string
        // TODO add string @res

        List<PredefinedText> data = new ArrayList<>();
        data.add(new PredefinedText(0, "An apple"));
        data.add(new PredefinedText(1, "B high"));
        data.add(new PredefinedText(2, "I want to sleep"));
        data.add(new PredefinedText(4, "5"));
        data.add(new PredefinedText(3, "4"));
        data.add(new PredefinedText(5, "6"));

        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) tts.setLanguage(Locale.US);
        });

        adapter = new PredefinedTextAdapter(data, tts);
        listView.setAdapter(adapter, true);
        listView.setCanDragHorizontally(false);
        listView.setCustomDragItem(new MyDragItem(this, R.layout.list_item_predefined_text));

//        String s = PredefinedText.toJson(data);
//        Log.e("erea", s);
//
//        List<PredefinedText> p = PredefinedText.fromJson(s);
//        for (PredefinedText i : p)
//            Log.e(i.getId() + "", i.getMessage());
    }

    private void addPredefinedText() {
        AlertDialog.Builder builder    = new AlertDialog.Builder(this);
        LayoutInflater      inflater   = LayoutInflater.from(this);
        View                add_dialog = inflater.inflate(R.layout.predefined_text_dialog, null);
        final EditText      message    = (EditText) add_dialog.findViewById(R.id.dialog_message);

        builder.setView(add_dialog)
                .setTitle(R.string.dialog_predefined_title)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_predefined_add, (dialog, i) -> {
                    int            id   = PredefinedText.getNextUniqueID(adapter.getItemList());
                    PredefinedText text = new PredefinedText(id, message.getText().toString());
                    adapter.addItem(INSERT_POS, text);
                    listView.getRecyclerView().smoothScrollToPosition(INSERT_POS);
                    dialog.cancel();
                })
                .setNegativeButton(R.string.dialog_predefined_cancel, (dialog, i) -> dialog.cancel())
                .create().show();
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
        else if (id == R.id.action_add_predefined_text) addPredefinedText();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
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
            TextView     v    = (TextView) dragView.findViewById(R.id.predefined_text);
            v.setText(text);
            v.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }
}
