package edu.ilstu.it.it226androidalarmclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AlarmSetupActivity extends AppCompatActivity {
    public EditText editTextMessage;
    public EditText editTextLocation;
    public EditText editTextTime;

    public static final String MESSAGE_EXTRA = "MESSAGE_EXTRA";

    String setMessage;
    String setLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setMessage = "";

        editTextTime = (EditText) findViewById(R.id.editTextTime);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);

        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        final Button buttonAlarmOkay = (Button) findViewById(R.id.buttonAlarmOkay);
        buttonAlarmOkay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setMessage = editTextMessage.getText().toString();
                setLocation = editTextLocation.getText().toString();
                String[] time = editTextTime.getText().toString().split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                MainActivity.instance.createAlarm(setMessage, datePicker.getMonth(), datePicker.getDayOfMonth(), datePicker.getYear(), hour, minute);
                finish();
            }
        });
        final Button buttonAlarmCancel = (Button) findViewById(R.id.buttonAlarmCancel);
        buttonAlarmCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
