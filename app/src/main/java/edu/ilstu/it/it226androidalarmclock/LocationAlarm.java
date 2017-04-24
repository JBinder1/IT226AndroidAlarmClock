package edu.ilstu.it.it226androidalarmclock;

import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.location.LocationListener;

public class LocationAlarm implements LocationListener {
    static private final long DEFAULT_MINUTES = 2;
    static private CountDownTimer locationTimer;
    static private long recentCustomMinutes;
    static boolean isGoingOff;

    static public void startLocationAlarm(){
        startLocationAlarm(DEFAULT_MINUTES);
    }

    static public void startLocationAlarm(long customMinutes){
        recentCustomMinutes = customMinutes;

        locationTimer = new CountDownTimer(customMinutes * 1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // MainActivity.instance.locationNotification(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                isGoingOff = true;
                MainActivity.instance.locationNotification();
            }
        }.start();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(isGoingOff){
            MainActivity.instance.notificationManager.cancel(2);
            locationTimer.cancel();
            isGoingOff = false;
        }
        MainActivity.instance.notificationManager.cancel(2);
        locationTimer.cancel();
        startLocationAlarm(recentCustomMinutes);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
