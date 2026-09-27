package com.zendegiyar.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReminderScheduler {

    public static void schedule(
            Context context,
            String id,
            String title,
            long triggerAtMillis,
            String repeat
    ) {

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) return;

        Intent intent = new Intent(context, ReminderReceiver.class);

        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("time",
                String.valueOf(triggerAtMillis));
        intent.putExtra("repeat", repeat);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        id.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                        PendingIntent.FLAG_IMMUTABLE
                );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            );
        } else {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            );
        }
    }

    public static void cancel(
            Context context,
            String id
    ) {

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(
                        Context.ALARM_SERVICE
                );

        if (alarmManager == null) return;

        Intent intent =
                new Intent(context, ReminderReceiver.class);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        id.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                        PendingIntent.FLAG_IMMUTABLE
                );

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void scheduleNext(
            Context context,
            String id,
            String title,
            String time,
            String repeat
    ) {

        try {

            long oldTime = Long.parseLong(time);

            Calendar next = Calendar.getInstance();
            next.setTimeInMillis(oldTime);

            Calendar now = Calendar.getInstance();

            if ("روزانه".equals(repeat)) {

                next.add(Calendar.DAY_OF_YEAR, 1);

            } else if ("هفتگی".equals(repeat)) {

                next.add(Calendar.WEEK_OF_YEAR, 1);

            } else if ("ماهانه".equals(repeat)) {

                next.add(Calendar.MONTH, 1);

            } else {

                return;
            }

            while (next.getTimeInMillis() <= now.getTimeInMillis()) {

                if ("روزانه".equals(repeat)) {
                    next.add(Calendar.DAY_OF_YEAR, 1);

                } else if ("هفتگی".equals(repeat)) {
                    next.add(Calendar.WEEK_OF_YEAR, 1);

                } else if ("ماهانه".equals(repeat)) {
                    next.add(Calendar.MONTH, 1);
                }
            }

            schedule(
                    context,
                    id,
                    title,
                    next.getTimeInMillis(),
                    repeat
            );

        } catch (Exception ignored) {
        }
    }
}
