package edu.ilstu.it.it226androidalarmclock;

import android.os.CountDownTimer;

public class LocationAlarm {
    private static final int DEFAULT_MINUTES = 2;
    private static CountDownTimer locationTimer;

    public static void startLocationAlarm(int customMinutes){
        locationTimer = new CountDownTimer(customMinutes * 1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO
                // .setText("Get moving in: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                // TODO If the location has not changed in the last ‘x’ minutes (‘x’ set by the user above in step a)
                // this alarm will trigger and display a message “Get up and walk!” The alarm will only stop if the
                // user changes the locations. After that, it goes back to monitoring the location movement.
            }
        }.start();
    }
}
