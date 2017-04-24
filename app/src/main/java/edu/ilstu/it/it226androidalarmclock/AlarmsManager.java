package edu.ilstu.it.it226androidalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static edu.ilstu.it.it226androidalarmclock.MainActivity.MESSAGE_EXTRA;

final class AlarmsManager {

    private final List<Alarm> alarms = new ArrayList<>();
    private final MainActivity mainActivity;

    private AlarmManager alarmManager;

    public AlarmsManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        alarmManager = (AlarmManager) mainActivity.getSystemService(ALARM_SERVICE);
    }

    void createAlarm(final String message, final int setYear, final int setMonth, final int setDay, final int setHour, final int setMinute) {
        final Intent myIntent = new Intent(mainActivity, AlarmReceiver.class);
        myIntent.putExtra(MESSAGE_EXTRA, message);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(mainActivity, 0, myIntent, 0);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(setYear, setMonth, setDay, setHour, setMinute);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + calendar.getTimeInMillis() - System.currentTimeMillis(), pendingIntent);
        alarms.add(new Alarm(message, calendar.getTimeInMillis(), myIntent, pendingIntent));

        mainActivity.googleCalendarManager.addEvent(message, calendar);
    }
}
