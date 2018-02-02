package aad.assignment.strokeassistant;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import aad.assignment.strokeassistant.model.HealthRecord;

/**
 * Created by chooh on 1/31/2018.
 */

public class HealthRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_FOOTER = 0,
            VIEW_TYPE_CELL = 2,
            BLOOD_PRESSURE_GRAPH = 0,
            BLOOD_SUGAR_GRAPH = 1,
            BMI_GRAPH = 2;
    private final String GRAPH_TYPE = "GRAPH_TYPE";
    private ArrayList<HealthRecord> records;
    private Context context;
    private ViewSwitcher viewSwitcher;

    HealthRecordAdapter(ArrayList<HealthRecord> records,
                        Context context,
                        ViewSwitcher viewSwitcher) {
        this.records = records;
        this.context = context;
        this.viewSwitcher = viewSwitcher;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView dateTextView;
        ImageView buttonDelete;

        ItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            dateTextView = (TextView) itemView.findViewById(R.id.record_date);
            buttonDelete = (ImageView) itemView.findViewById(R.id.btn_text_delete);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        Button buttonBmi, buttonBloodSugar, buttonBloodPressure;

        FooterViewHolder(View itemView) {
            super(itemView);
            buttonBmi = (Button) itemView.findViewById(R.id.button_bmi);
            buttonBloodSugar = (Button) itemView.findViewById(R.id.button_blood_sugar);
            buttonBloodPressure = (Button) itemView.findViewById(R.id.button_blood_pressure);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == records.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == VIEW_TYPE_CELL) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_cards, parent, false);
            return new ItemViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_graph_button, parent, false);
            return new FooterViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder healthViewHolder,
                                 int index) {

        if (healthViewHolder instanceof ItemViewHolder) {
            HealthRecord   record = records.get(index);
            DateFormat     df     = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            String         date   = "Record " + (index + 1) + " From: " + df.format(record.getDate());
            ItemViewHolder holder = (ItemViewHolder) healthViewHolder;
            holder.dateTextView.setText(date);

            holder.dateTextView.setOnClickListener(v -> {
                AlertDialog.Builder builder            = new AlertDialog.Builder(context);
                LayoutInflater      inflater           = LayoutInflater.from(context);
                View                healthRecordDialog = inflater.inflate(R.layout.health_record_dialog, null);
                ((TextView) healthRecordDialog.findViewById(R.id.blood_pressure)).setText(Integer.toString(record.getBloodPressure()));
                ((TextView) healthRecordDialog.findViewById(R.id.blood_sugar)).setText(Integer.toString(record.getBloodSugar()));
                ((TextView) healthRecordDialog.findViewById(R.id.body_weight)).setText(Float.toString(record.getBodyWeight()));
                ((TextView) healthRecordDialog.findViewById(R.id.body_height)).setText(Float.toString(record.getBodyHeight()));
                ((TextView) healthRecordDialog.findViewById(R.id.bmi)).setText(Float.toString(record.getBmi()));
                ((TextView) healthRecordDialog.findViewById(R.id.date)).setText(date);

                builder.setView(healthRecordDialog)
                        .setTitle(R.string.dialog_view_health_rec_title)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_ok, (dialog, i) -> {
                            dialog.cancel();
                        })
                        .create().show();
            });

            holder.buttonDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(R.string.dialog_delete_health_rec_title)
                        .setMessage(R.string.dialog_health_delete_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_yes, (dialog, i) -> {
                            records.remove(index);
                            HealthRecord.save(context, records);
                            notifyDataSetChanged();
                            if (records.isEmpty()) viewSwitcher.showNext();
                        })
                        .create().show();
            });
        } else if (healthViewHolder instanceof FooterViewHolder) {
            FooterViewHolder holder = (FooterViewHolder) healthViewHolder;

            holder.buttonBmi.setOnClickListener(v -> {
                Intent intent = new Intent(context, GraphActivity.class);
                intent.putExtra(GRAPH_TYPE, BMI_GRAPH);
                context.startActivity(intent);
            });

            holder.buttonBloodSugar.setOnClickListener(v -> {
                Intent intent = new Intent(context, GraphActivity.class);
                intent.putExtra(GRAPH_TYPE, BLOOD_SUGAR_GRAPH);
                context.startActivity(intent);
            });

            holder.buttonBloodPressure.setOnClickListener(v -> {
                Intent intent = new Intent(context, GraphActivity.class);
                intent.putExtra(GRAPH_TYPE, BLOOD_PRESSURE_GRAPH);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return records.size() + 1;
    }

}
