package com.zendegiyar.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())
                || Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {

            // برنامه بعد از روشن شدن گوشی آماده دریافت یادآوری‌هاست.
            // یادآوری‌هایی که توسط ReminderScheduler ثبت شده‌اند
            // در سیستم اندروید باقی می‌مانند.
        }
    }
}
