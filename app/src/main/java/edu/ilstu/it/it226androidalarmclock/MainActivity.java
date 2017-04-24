package edu.ilstu.it.it226androidalarmclock;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final String MESSAGE_EXTRA = "MESSAGE_EXTRA";

    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";

    private AlarmManager alarmManager;
    private NotificationManager notificationManager;
    private static int notificationIdNum;

    static MainActivity instance;

    private List<Alarm> alarms;

    GoogleAccountCredential mCredential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button buttonNewAlarm = (Button) findViewById(R.id.buttonNewAlarm);
        final Button buttonNewTimer = (Button) findViewById(R.id.buttonNewTimer);
        final Button buttonConfigureLocationAlarm = (Button) findViewById(R.id.buttonConfigureLocationAlarm);
        buttonNewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(this, AlarmSetupActivity.class);
                // TODO
            }
        });
        buttonNewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO open dialogue/activity to set new timer
                // TODO create activity for New Timer
            }
        });
        buttonConfigureLocationAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO open dialogue/activity to config location alarm
                // TODO create activity for Location Alarm Settings
            }
        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationIdNum = 0;

        alarms = new ArrayList<>();

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        acquireGooglePlayServices();
        chooseAccount();

        new MakeRequestTask(mCredential, "test!", Calendar.getInstance()).execute();
        // LocationAlarm.startLocationAlarm();
        // locationNotification();
    }

    // TODO make this display custom message
    void alarmNotification(String message) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.alarm_notification_title))
                .setContentText(message);
        notificationManager.notify(notificationIdNum++, builder.build());
    }

    void locationNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.location_notification_title))
                .setContentText(getResources().getString(R.string.location_notification_content))
                .setOngoing(true);
        notificationManager.notify(2, builder.build());
    }

    void createAlarm(final String message, final int setYear, final int setMonth, final int setDay, final int setHour, final int setMinute) {
        final Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra(MESSAGE_EXTRA, message);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(setYear, setMonth, setDay, setHour, setMinute);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + calendar.getTimeInMillis() - System.currentTimeMillis(), pendingIntent);
        alarms.add(new Alarm(message, calendar.getTimeInMillis(), myIntent, pendingIntent));

        new MakeRequestTask(mCredential, message, calendar).execute();
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

        switch (item.getItemId()) {
            case R.id.action_about:
                new AboutDialogFragment().show(getSupportFragmentManager(), "");
                return super.onOptionsItemSelected(item);
            case R.id.action_settings:
                // TODO if we're going to have a settings menu, instantiate it here
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                    }
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            // View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new AlarmSetupActivity.TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    /**
     * A dialog fragment containing info about the app and its creators.
     */
    public static class AboutDialogFragment extends DialogFragment {
        @Override
        public AlertDialog onCreateDialog(Bundle savedInterfaceInstance) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_about_text).setPositiveButton(R.string.dialog_about_positive, null);
            return builder.create();
        }
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;
        private String message;
        private Calendar calendar;

        MakeRequestTask(GoogleAccountCredential credential, String message, Calendar calendar) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
            this.message = message;
            this.calendar = calendar;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                getDataFromApi();
                return new ArrayList<>();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> output) {
            if (output == null || output.size() == 0) {
                Log.d("MakeRequestTask", "No results gathered");
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                Log.d("MakeRequestTask", TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    Log.d("MakeRequestTask", "Google Play Services unavailable!");
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    Log.d("MakeRequestTask", "The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Log.d("MakeRequestTask", "Request cancelled");
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         *
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private void getDataFromApi() throws IOException {
            Event event = new Event()
                    .setSummary(message);

            DateTime startDateTime = new DateTime(calendar.getTime());
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime);
            event.setStart(start);
            event.setEnd(start);

            String calendarId = "primary";
            event = mService.events().insert(calendarId, event).execute();
            Log.d("MakeRequestTask", "Event created: %s\n" + event.getHtmlLink());
        }
    }

}
