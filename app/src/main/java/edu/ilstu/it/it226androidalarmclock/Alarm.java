package edu.ilstu.it.it226androidalarmclock;

import android.app.PendingIntent;
import android.content.Intent;

final class Alarm {

    final String message;
    final long time;
    final Intent intent;
    final PendingIntent pendingIntent;

    Alarm(String message, long time, Intent intent, PendingIntent pendingIntent) {
        this.message = message;
        this.time = time;
        this.intent = intent;
        this.pendingIntent = pendingIntent;
    }
}
