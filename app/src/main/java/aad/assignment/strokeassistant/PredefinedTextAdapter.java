package aad.assignment.strokeassistant;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

import aad.assignment.strokeassistant.model.PredefinedText;

public class PredefinedTextAdapter extends DragItemAdapter<PredefinedText, PredefinedTextAdapter.ViewHolder> {
    private TextToSpeech tts;

    PredefinedTextAdapter(List<PredefinedText> data,
                          TextToSpeech tts) {
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
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).getId();
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        private TextView text;

        ViewHolder(View v) {
            super(v, R.id.predefined_text, true);
            text = (TextView) v.findViewById(R.id.predefined_text);
        }
    }

}
