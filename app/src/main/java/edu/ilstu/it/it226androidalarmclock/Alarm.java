package edu.ilstu.it.it226androidalarmclock;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;

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

//        final String msgPasser = message;
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable(){
//            final String msgPassed = msgPasser;
//            @Override
//            public void run() {
//                MainActivity.instance.alarmNotification(msgPassed);
//            }
//        }, time*1000);
    }




}
