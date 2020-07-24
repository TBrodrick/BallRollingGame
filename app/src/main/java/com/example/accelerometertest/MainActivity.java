package com.example.accelerometertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private SensorManager myManager;
    private Sensor mAcc;
    TextView valShow;
    Toast t = Toast.makeText(this, "test", Toast.LENGTH_SHORT);

    private SensorEventListener mAccEvenListener = new SensorEventListener() {
        @Override
        public final void onSensorChanged(SensorEvent event) {
            valShow.setText(event.toString());
            t.show();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        valShow = (TextView) findViewById(R.id.val);
        valShow.setText("Test");
        myManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAcc = myManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }
}