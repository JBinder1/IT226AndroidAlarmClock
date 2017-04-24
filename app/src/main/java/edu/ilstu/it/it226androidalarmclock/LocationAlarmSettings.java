package edu.ilstu.it.it226androidalarmclock;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LocationAlarmSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alarm_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText editTextLocationMinutes = (EditText) findViewById(R.id.editTextLocationMinutes);
        Button buttonOkay = (Button) findViewById(R.id.buttonOkay);
        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextLocationMinutes.getText().toString().isEmpty()){
                    LocationAlarm.startLocationAlarm(Long.parseLong(editTextLocationMinutes.getText().toString()));
                    finish();
                }else{
                    Snackbar.make(v, R.string.error_alarm_setup, Snackbar.LENGTH_LONG);
                }
            }
        });
    }

}
