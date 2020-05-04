package com.example.tamrin2.alarmFeature;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tamrin2.R;

import java.util.List;

public class AlarmActivity extends Activity {

    SensorManager sensorManager = null;
    List list;
    Intent myServiceIntent;
    float zAxisVelocity;
    double velocityLimit;

    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        animateBell();

        Intent intent = getIntent();
        velocityLimit = intent.getDoubleExtra("velocity limit", 0);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values = sensorEvent.values;
                zAxisVelocity = values[2] < 0 ? -values[2] : values[2];

                if (zAxisVelocity >= velocityLimit) {
                    finish();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        handleSensors();
        startService();

    }

    public void animateBell() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_clock);
        ImageView imgBell= findViewById(R.id.clock);
        imgBell.setImageResource(R.mipmap.clock);
        imgBell.setAnimation(shake);
    }

    public void startService() {
        myServiceIntent = new Intent(this, AlarmService.class);
        startService(myServiceIntent);
    }

    public void handleSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        list = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        if(list.size()>0){
            sensorManager.registerListener(sensorEventListener,
                    (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            Toast.makeText(getBaseContext(), "Error: No Accelerometer.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int milliSeconds = 10*60*1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, milliSeconds);
    }

    protected void onStop() {
        if(list.size()>0){
            sensorManager.unregisterListener(sensorEventListener);
        }
        stopService(myServiceIntent);

//        System.out.println("_________________________ areeeeeeeeeeeeeeeeeeeeeee?");
        cancelAlarm();
        super.onStop();
    }


    public void cancelAlarm() {
//        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(AlarmFragment.getMyContext(), AlarmReceiver.class);
        intent.setAction("AlarmStarted");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                AlarmFragment.getMyContext(), AlarmFragment.pendingIntentCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.cancel(pendingIntent);
        AlarmFragment.getAlarmManager().cancel(pendingIntent);
    }
}