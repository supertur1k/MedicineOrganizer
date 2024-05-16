package com.example.medicineorganizer.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NotificationScheduler {
    private static final String TAG = "NotificationScheduler";

    public static void scheduleNotification(Context context, String message, LocalDateTime dateTime) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("message", message);

        int notificationId = (int) System.currentTimeMillis();
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            long triggerAtMillis = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                triggerAtMillis = ZonedDateTime.of(dateTime, ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            Log.d(TAG, "Scheduling notification for: " + dateTime + " with ID: " + notificationId);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        } else {
            Log.e(TAG, "AlarmManager is null");
        }
    }
}