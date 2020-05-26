package trungatom.tqt.secondapp.background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import trungatom.tqt.secondapp.activities.MainActivity;

public class ReminderService extends BroadcastReceiver {
    private final int NOTIFICATION_ID = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("checka", "onReceive: ");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String chanelId = "noti.toeic.default";
        String chanelTitle = "Reminder";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelId);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(chanelId, chanelTitle, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Intent intentGoToApp = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intentGoToApp, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, chanelId);
        builder.setContentTitle("Time to study")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentText("Tap to start")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
