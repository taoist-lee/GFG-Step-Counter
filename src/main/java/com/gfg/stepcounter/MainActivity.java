package com.gfg.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView steps, count;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private boolean sensorFound;
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hooks
        steps = findViewById(R.id.steps);
        count = findViewById(R.id.count);

        //Setting Up the Sensor Manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Checking the availability of the sensor on the device
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null) {
            stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            sensorFound = true;
        }
        else {
            Toast.makeText(getApplicationContext(), "Required System Sensor Not Found!", Toast.LENGTH_LONG).show();
            sensorFound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Registering the sensor on Resume.
        if(sensorFound) {
            sensorManager.registerListener(this, stepCountSensor, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unregistering the sensor on Pause. Prevents battery drainage in background.
        if(sensorFound) {
            sensorManager.unregisterListener(this, stepCountSensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == stepCountSensor) {
            stepCount = (int) event.values[0];
            count.setText(String.valueOf(stepCount));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // won't happen
    }
}