package edu.ilstu.it.it226androidalarmclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        MainActivity.instance.testNotification();
        setResultCode(Activity.RESULT_OK);
    }
}