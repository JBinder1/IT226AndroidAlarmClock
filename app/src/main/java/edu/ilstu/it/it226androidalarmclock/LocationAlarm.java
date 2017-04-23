package edu.ilstu.it.it226androidalarmclock;

import android.content.Intent;
import android.location.Location;
import android.os.CountDownTimer;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

public class LocationAlarm {
    private static final long DEFAULT_MINUTES = 2;
    private CountDownTimer locationTimer;

    public void startLocationAlarm(){
        startLocationAlarm(DEFAULT_MINUTES);
    }

    public void startLocationAlarm(long customMinutes){
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(null)
//                .setContentTitle("Get up and walk!").setContentText("Your location alarm has ended.");
//        Intent resultIntent = new Intent(this, ResultActivity.class);
//        locationTimer = new CountDownTimer(customMinutes * 1000 * 60, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                // TODO possible notification about how long until location alarm goes off?
//            }
//
//            @Override
//            public void onFinish() {
//                // TODO set off notification as described on assignment pdf
//                // If the location has not changed in the last ‘x’ minutes (‘x’ set by the user above in step a)
//                // this alarm will trigger and display a message “Get up and walk!” The alarm will only stop if the
//                // user changes the locations. After that, it goes back to monitoring the location movement.
//
//            }
//        }.start();
    }
}
