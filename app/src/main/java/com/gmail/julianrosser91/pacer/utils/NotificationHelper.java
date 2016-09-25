package com.gmail.julianrosser91.pacer.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.gmail.julianrosser91.pacer.R;
import com.gmail.julianrosser91.pacer.main.view.MainActivity;

public class NotificationHelper {

    public static int TRACKING_ID = 100;

    public static void showTrackingNotification(Context context, String message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_run_notification)
                        .setContentTitle("Pace Calculator")
                        .setContentText(message)
                        .setOngoing(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setContentText(message);

        // Because the ID remains unchanged, the existing notification is
        // updated.
        mNotificationManager.notify(TRACKING_ID, mBuilder.build());
    }

    public static void removeTrackingNotification(Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(TRACKING_ID);
    }

    public static String getMessageFromLocation() {
        return "3.9km - Time: 18:34 - Pace: 6:04";
    }

}
