package edu.ilstu.it.it226androidalarmclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        MainActivity.instance.alarmNotification(intent.getStringExtra(MainActivity.MESSAGE_EXTRA));
        setResultCode(Activity.RESULT_OK);
    }
}
