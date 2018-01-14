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
import android.widget.ViewSwitcher;

import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;

import java.util.List;
import java.util.Locale;

import aad.assignment.strokeassistant.model.PredefinedText;

public class PredefinedTextActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private ViewSwitcher viewSwitcher;
    private DragListView listView;
    private PredefinedTextAdapter adapter;
    private final Context context = PredefinedTextActivity.this;
    private static final int INSERT_POS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predefined_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final float          dp      = getResources().getDisplayMetrics().density;
        final int            PADDING = (int) (8 * dp);
        List<PredefinedText> data    = PredefinedText.load(context);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.vs_predefined_text);
        if (data.isEmpty()) viewSwitcher.showNext();

        listView = (DragListView) findViewById(R.id.list_predefined_text);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.getRecyclerView().setPadding(PADDING, PADDING, PADDING, PADDING);
        listView.getRecyclerView().setClipToPadding(false);

        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.US);
                tts.setSpeechRate(0.75f);
            }
        });

        adapter = new PredefinedTextAdapter(context, data, tts, viewSwitcher);
        listView.setAdapter(adapter, true);
        listView.setCanDragHorizontally(false);
        listView.setCustomDragItem(new MyDragItem(context, R.layout.list_item_predefined_text));
        listView.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragEnded(int fromPosition,
                                        int toPosition) {
                super.onItemDragEnded(fromPosition, toPosition);
                PredefinedText.save(context, adapter.getItemList());
            }
        });
    }

    private void addPredefinedText() {
        AlertDialog.Builder builder    = new AlertDialog.Builder(context);
        LayoutInflater      inflater   = LayoutInflater.from(context);
        View                add_dialog = inflater.inflate(R.layout.predefined_text_dialog, null);
        final EditText      message    = (EditText) add_dialog.findViewById(R.id.dialog_message);

        builder.setView(add_dialog)
                .setTitle(R.string.dialog_predefined_title)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_predefined_add, (dialog, i) -> {
                    String msg = message.getText().toString().trim();

                    if (!msg.isEmpty()) {
                        int            id   = PredefinedText.getNextUniqueID(adapter.getItemList());
                        PredefinedText text = new PredefinedText(id, msg);

                        adapter.addItem(INSERT_POS, text);
                        listView.getRecyclerView().smoothScrollToPosition(INSERT_POS);
                        PredefinedText.save(context, adapter.getItemList());

                        if (adapter.getItemCount() == 1) viewSwitcher.showNext();
                    }
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
