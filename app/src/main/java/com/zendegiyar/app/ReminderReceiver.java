package com.zendegiyar.app;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "zendegiyar_reminders";

    @Override
    public void onReceive(Context context, Intent intent) {

        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        String repeat = intent.getStringExtra("repeat");

        showNotification(
                context,
                "⏰ زندگی‌یار",
                title == null ? "یک یادآوری داری" : title
        );

        if (id != null && repeat != null && !"یک‌بار".equals(repeat)) {
            ReminderScheduler.scheduleNext(
                    context,
                    id,
                    title,
                    time,
                    repeat
            );
        }
    }

    static void showNotification(
            Context context,
            String title,
            String body
    ) {

        if (Build.VERSION.SDK_INT >= 33 &&
                context.checkSelfPermission(
                        Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationManager nm =
                (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (nm == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "یادآوری‌های زندگی‌یار",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            channel.setDescription(
                    "اعلان یادآوری‌های زندگی‌یار"
            );

            nm.createNotificationChannel(channel);
        }

        Intent openIntent =
                new Intent(context, MainActivity.class);

        openIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        PendingIntent contentIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        openIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                        PendingIntent.FLAG_IMMUTABLE
                );

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(
                    context,
                    CHANNEL_ID
            );
        } else {
            builder = new Notification.Builder(context);
        }

        builder
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT);

        nm.notify(
                (int) (System.currentTimeMillis() & 0x7fffffff),
                builder.build()
        );
    }
}
