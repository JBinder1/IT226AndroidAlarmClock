package edu.ilstu.it.it226androidalarmclock;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class TimerSetupActivity extends AppCompatActivity {
    String setMessage;
    String setLocation;
    int setMinute;
    EditText editTextMinutes;
    EditText editTextMessage;
    EditText editTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setMessage = "";
        setLocation = "";
        setMinute = 0;

        editTextMinutes = (EditText) findViewById(R.id.editTextMinute);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        final Button buttonAlarmOkay = (Button) findViewById(R.id.buttonAlarmOkay);
        buttonAlarmOkay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    setMinute = Integer.parseInt(editTextMinutes.getText().toString());
                    setLocation = editTextLocation.toString();
                    setMessage = editTextMessage.getText().toString();
                }catch(Exception e){
                    Snackbar.make(v, getResources().getString(R.string.error_alarm_setup), Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, setMinute);

                MainActivity.instance.createAlarm(setMessage, setLocation, cal);
                finish();
            }
        });
    }

    public void cancelSetup(){
        setContentView(super.findViewById(R.id.main_content));
    }
}
