package edu.ilstu.it.it226androidalarmclock;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimerSetupActivity extends AppCompatActivity {
    String setMessage;
    int setMinute;
    EditText editTextMinutes;
    EditText editTextMessage;
    // EditText editTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setMessage = "";
        setMinute = -1;

        editTextMinutes = (EditText) findViewById(R.id.editTextMinute);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        // editTextLocation = (EditText) findViewById(R.id.editTextLocation);

        final Button buttonAlarmOkay = (Button) findViewById(R.id.buttonAlarmOkay);
        buttonAlarmOkay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    setMinute = Integer.parseInt(editTextMinutes.getText().toString());
                    setMessage = editTextMessage.getText().toString();
                    // setLocation = editTextLocation.toString();
                }catch(Exception e){
                    Snackbar.make(v, getResources().getString(R.string.error_alarm_setup), Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.DAY_OF_MONTH);
                int minute = cal.get(Calendar.MINUTE) + setMinute;

                if(minute > 60){
                    minute-=60;
                    hour++;
                }
                if(hour > 24){
                    hour-=24;
                    day++;
                }

                if(setMinute > -1){
                    MainActivity.instance.createAlarm(setMessage, year, month, day, hour, minute);
                    finish();
                }else {
                    Snackbar.make(v, getResources().getString(R.string.error_alarm_setup), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancelSetup(){
        setContentView(super.findViewById(R.id.main_content));
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInterfaceState){
            final Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        }
    }
}
