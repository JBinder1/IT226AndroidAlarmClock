package edu.ilstu.it.it226androidalarmclock;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
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

public class AlarmSetupActivity extends AppCompatActivity {
    public EditText editTextTime;
    public EditText editTextDate;
    public EditText editTextMessage;

    int setDay;
    int setMonth;
    int setYear;
    int setHour;
    int setMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setDay = -1;
        setMonth = -1;
        setYear = -1;
        setHour = -1;
        setMinute = -1;

        editTextTime = (EditText) findViewById(R.id.editTextTime);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        final Button buttonAlarmOkay = (Button) findViewById(R.id.buttonAlarmOkay);
        buttonAlarmOkay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(setDay > -1 && setMonth > -1 && setYear > -1 && setHour > -1 && setMinute > -1){
                    // TODO create new alarm from this info
                    cancelSetup();
                }else {
                    Snackbar.make(v, getResources().getString(R.string.error_alarm_setup), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        final Button buttonAlarmCancel = (Button) findViewById(R.id.buttonAlarmCancel);
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

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            editTextDate.setText(month + " / " + dayOfMonth + " / " + year);
            setDay = dayOfMonth;
            setMonth = month;
            setYear = year;
        }
    };

    private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            editTextTime.setText(hourOfDay + ":" + minute);
            setHour = hourOfDay;
            setMinute = minute;
        }
    };
}
