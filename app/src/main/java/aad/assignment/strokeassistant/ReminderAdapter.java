package aad.assignment.strokeassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import aad.assignment.strokeassistant.model.Reminder;

/**
 * Created by chooh on 2/2/2018.
 */

public class ReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Reminder> reminders;
    private Context context;
    private ViewSwitcher viewSwitcher;

    ReminderAdapter(ArrayList<Reminder> reminders, Context context, ViewSwitcher viewSwitcher) {
        this.reminders = reminders;
        this.context = context;
        this.viewSwitcher = viewSwitcher;

    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView dateTextView;
        ImageView buttonDelete;

        ReminderViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            dateTextView = (TextView) itemView.findViewById(R.id.reminder_title);
            buttonDelete = (ImageView) itemView.findViewById(R.id.btn_text_delete);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_cards, parent, false);
        return new ReminderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder reminderViewHolder, int index) {

        Reminder record = reminders.get(index);
        ReminderViewHolder holder = (ReminderViewHolder) reminderViewHolder;
        holder.dateTextView.setText(record.getTitle());

        holder.dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View reminderDialog = inflater.inflate(R.layout.reminder_dialog, null);


                EditText title = (EditText) reminderDialog.findViewById(R.id.reminder_title);
                EditText description = (EditText) reminderDialog.findViewById(R.id.reminder_description);
                TextView time = (TextView) reminderDialog.findViewById(R.id.reminder_time);

                title.setText(reminders.get(index).getTitle());
                title.setEnabled(false);
                title.setTextColor(Color.BLACK);

                description.setText(reminders.get(index).getDescription());
                description.setEnabled(false);
                description.setTextColor(Color.BLACK);

                time.setText(Integer.toString(reminders.get(index).getHour()) + " : " + Integer.toString(reminders.get(index).getMinute()));
                time.setTextColor(Color.BLACK);

                builder.setView(reminderDialog)
                        .setTitle(R.string.dialog_view_health_rec_title)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_ok, (dialog, i) -> {
                            dialog.cancel();
                        })
                        .create().show();
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder
                        .setTitle(R.string.dialog_delete_health_rec_title)
                        .setMessage(R.string.dialog_health_delete_message)
                        .setCancelable(true)
                        .setPositiveButton(R.string.dialog_yes, (dialog, i) -> {
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                            Intent updateServiceIntent = new Intent(context, MainActivity.class);
                            PendingIntent pendingUpdateIntent = PendingIntent.getService(context, reminders.get(index).getId(), updateServiceIntent, 0);

                            try {
                                alarmManager.cancel(pendingUpdateIntent);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.reminder_removal_error, Toast.LENGTH_LONG);
                            }

                            reminders.remove(index);
                            Reminder.save(context, reminders);
                            notifyDataSetChanged();
                            if (reminders.isEmpty()) viewSwitcher.showNext();
                        })
                        .create().show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

}

