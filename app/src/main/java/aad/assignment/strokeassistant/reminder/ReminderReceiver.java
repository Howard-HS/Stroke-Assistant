package aad.assignment.strokeassistant.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import aad.assignment.strokeassistant.MainActivity;
import aad.assignment.strokeassistant.R;
import aad.assignment.strokeassistant.ReminderActivity;

/**
 * Created by chooh on 2/3/2018.
 */

public class ReminderReceiver extends BroadcastReceiver {

    private static final String REMINDER_TITLE = "REMINDER_TITLE",
            REMINDER_DESCRIPTION = "REMINDER_DESCRIPTION";

    @Override
    public void onReceive(Context context,
                          Intent intent) {
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.blue_ball)
                        .setContentTitle(intent.getExtras().getString(REMINDER_TITLE))
                        .setContentText(intent.getExtras().getString(REMINDER_DESCRIPTION));
        Intent           resultIntent = new Intent(context, ReminderActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(intent.getExtras().getInt(REMINDER_TITLE), mBuilder.build());
    }
}
