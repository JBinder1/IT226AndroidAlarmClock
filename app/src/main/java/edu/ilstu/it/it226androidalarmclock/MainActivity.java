package edu.ilstu.it.it226androidalarmclock;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private NotificationManager notificationManager;

    static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button buttonNewAlarm = (Button) findViewById(R.id.buttonNewAlarm);
        final Button buttonNewTimer = (Button) findViewById(R.id.buttonNewTimer);
        final Button buttonConfigureLocationAlarm = (Button) findViewById(R.id.buttonConfigureLocationAlarm);
        buttonNewAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO open dialogue/activity to set new alarm
            }
        });
        buttonNewTimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO open dialogue/activity to set new timer
            }
        });
        buttonConfigureLocationAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // TODO open dialogue/activity to config location alarm
            }
        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        instance = this;
        createAlarmElapsed(5 * 1000);
    }

    void testNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Alarm")
                .setContentText("Custom alarm message here!");
        notificationManager.notify(0, builder.build());
    }

    void createAlarmElapsed(final long time) {
        final Intent myIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()){
            case R.id.action_about:
                new AboutDialogFragment().show(getSupportFragmentManager(), "");
                return super.onOptionsItemSelected(item);
            //case R.id.action_settings:
            //    break;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A dialog fragment containing info about the app and its creators.
     */
    public static class AboutDialogFragment extends DialogFragment {
        @Override
        public AlertDialog onCreateDialog(Bundle savedInterfaceInstance){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_about_text).setPositiveButton(R.string.dialog_about_positive, null);
            return builder.create();
        }
    }
}
