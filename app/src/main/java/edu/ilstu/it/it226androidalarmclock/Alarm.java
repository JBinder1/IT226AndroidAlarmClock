package edu.ilstu.it.it226androidalarmclock;

import android.app.PendingIntent;
import android.content.Intent;

final class Alarm {

    private String message;
    private final String location;
    private final long time;
    private final Intent intent;
    private final PendingIntent pendingIntent;

    Alarm(String message, String location, long time, Intent intent, PendingIntent pendingIntent) {
        this.message = message;
        this.location = location;
        this.time = time;
        this.intent = intent;
        this.pendingIntent = pendingIntent;
    }
}
