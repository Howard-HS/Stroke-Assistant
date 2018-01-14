package aad.assignment.strokeassistant;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

import aad.assignment.strokeassistant.model.PredefinedText;

public class PredefinedTextAdapter extends DragItemAdapter<PredefinedText, PredefinedTextAdapter.ViewHolder> {
    private Context context;
    private TextToSpeech tts;

    PredefinedTextAdapter(Context context,
                          List<PredefinedText> data,
                          TextToSpeech tts) {
        this.context = context;
        this.tts = tts;
        setItemList(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_predefined_text, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {
        super.onBindViewHolder(holder, position);
        final PredefinedText obj = mItemList.get(position);

        holder.text.setText(obj.getMessage());
        holder.text.setOnClickListener(view -> tts.speak(obj.getMessage(), TextToSpeech.QUEUE_FLUSH, null));

        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(R.string.dialog_predefined_delete_title)
                    .setMessage(R.string.dialog_predefined_delete_message)
                    .setCancelable(true)
                    .setPositiveButton(R.string.dialog_yes, (dialog, i) -> {
                        removeItem(position);
                        notifyDataSetChanged();
                        PredefinedText.save(context, getItemList());
                    })
                    .setNegativeButton(R.string.dialog_no, (dialog, i) -> dialog.cancel())
                    .create().show();
        });
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).getId();
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        private TextView text;
        private ImageView btnDelete;

        ViewHolder(View v) {
            super(v, R.id.predefined_text, true);
            text = (TextView) v.findViewById(R.id.predefined_text);
            btnDelete = (ImageView) v.findViewById(R.id.btn_text_delete);
        }
    }

}
