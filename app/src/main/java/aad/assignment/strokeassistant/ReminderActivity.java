package aad.assignment.strokeassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Calendar;

import aad.assignment.strokeassistant.model.Reminder;
import aad.assignment.strokeassistant.reminder.ReminderReceiver;

public class ReminderActivity extends AppCompatActivity {

    private ArrayList<Reminder> reminders;
    private ViewSwitcher viewSwitcher;
    private Context context = ReminderActivity.this;
    private RecyclerView recyclerView;

    private static final String REMINDER_TITLE = "REMINDER_TITLE",
            REMINDER_DESCRIPTION = "REMINDER_DESCRIPTION",
            REMINDER_ID = "REMINDER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminders = Reminder.load(this);
        setContentView(R.layout.activity_reminder);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.vs_reminder);

        if (!reminders.isEmpty()) viewSwitcher.showNext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        reminders = Reminder.load(this);
        ReminderAdapter adapter = new ReminderAdapter(reminders, context, viewSwitcher);

        recyclerView = (RecyclerView) findViewById(R.id.reminder_cards);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.health_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) finish();
        else if (id == R.id.action_add_health_record) addHealthRecord();

        return super.onOptionsItemSelected(item);
    }

    private void addHealthRecord() {
        AlertDialog.Builder builder     = new AlertDialog.Builder(context);
        LayoutInflater      inflater    = LayoutInflater.from(context);
        View                add_dialog  = inflater.inflate(R.layout.reminder_dialog, null);
        EditText            title       = (EditText) add_dialog.findViewById(R.id.reminder_title);
        EditText            description = (EditText) add_dialog.findViewById(R.id.reminder_description);
        TextView            time        = (TextView) add_dialog.findViewById(R.id.reminder_time);

        time.setOnClickListener(v -> {
            Calendar         mcurrentTime = Calendar.getInstance();
            int              hour         = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int              minute       = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(context,
                    (timePicker, selectedHour, selectedMinute) -> time.setText(String.format("%d:%d", selectedHour, selectedMinute)),
                    hour, minute, true); // Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        });
        builder.setView(add_dialog)
                .setTitle(R.string.add_reminder)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_predefined_add, (dialog, i) -> {
                    String titleValue       = title.getText().toString().trim();
                    String descriptionValue = description.getText().toString().trim();
                    String timeValue        = time.getText().toString().trim();

                    if (!titleValue.isEmpty() && !descriptionValue.isEmpty() && timeValue.contains(":")) {
                        String[] splitTime = timeValue.split(":");
                        String   hour      = splitTime[0];
                        String   minute    = splitTime[1];

                        Reminder reminder = new Reminder(titleValue, descriptionValue, Integer.parseInt(hour), Integer.parseInt(minute));
                        reminders.add(reminder);
                        Reminder.save(this, reminders);

                        reminders = Reminder.load(this);
                        recyclerView.setAdapter(new ReminderAdapter(reminders, context, viewSwitcher));
                        setAlarm(reminder);
                        if (reminders.size() == 1) viewSwitcher.showNext();
                    } else {
                        Toast.makeText(context, R.string.empty_field, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_predefined_cancel, (dialog, i) -> dialog.cancel())
                .create().show();

    }


    private void setAlarm(Reminder reminder) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        calendar.set(Calendar.MINUTE, reminder.getMinute());
        calendar.set(Calendar.SECOND, 0);

        long   notificationTime = calendar.getTimeInMillis();
        Intent intentAlarm      = new Intent(this, ReminderReceiver.class);
        intentAlarm.putExtra(REMINDER_TITLE, reminder.getTitle());
        intentAlarm.putExtra(REMINDER_DESCRIPTION, reminder.getDescription());
        intentAlarm.putExtra(REMINDER_ID, reminder.getId());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, notificationTime, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(this, reminder.getId(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
