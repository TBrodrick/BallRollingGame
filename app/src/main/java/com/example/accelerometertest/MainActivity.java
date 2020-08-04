//lines I changed: 18, 26, 35 - 36, 39 - 51, 55 - 63
package com.example.accelerometertest;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager myManager;
    private Sensor myAcc;
    private ImageView gameBall;
    private ImageView[] Walls;
    private int[] ballLocation = {0, 0};
    private float speedX;
    private float speedY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        myAcc = myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myManager.registerListener(this, myAcc, SensorManager.SENSOR_DELAY_NORMAL);
        gameBall = (ImageView)findViewById(R.id.ball);
        Walls = new ImageView[1];
        ImageView wall = (ImageView)findViewById(R.id.imageView);
        Walls[0] = wall;
    }

    public void moveBall(float valX, float valY){
        float speedMoveX = (float)(-571.4 * valX);
        float speedMoveY = (float)(571.4 * valY);

        if(speedMoveX > 0){
            for(int i = 0; i < Walls.length; i++){

            }
        }
        else{
            for(int i = 0; i < Walls.length; i++){

            }
        }
        if(speedMoveY > 0){
            for(int i = 0; i < Walls.length; i++){
                if(Walls[i].getTop() <= ballLocation[1]
                    && Walls[i].getBottom() > (ballLocation[1] + gameBall.getMeasuredHeight())) {
                    if((Walls[i].getLeft() <= (ballLocation[0] + gameBall.getMeasuredWidth()))
                            && (Walls[i].getRight() > ballLocation[0]))
                        speedMoveY = 0;
                }
            }
        }
        else{
            for(int i = 0; i < Walls.length; i++){
                if(Walls[i].getBottom() >= (ballLocation[1] + gameBall.getMaxHeight())) {
                    if((Walls[i].getLeft() <= (ballLocation[0] + gameBall.getMeasuredWidth()))
                            && (Walls[i].getRight() > ballLocation[0]))
                        speedMoveY = 0;
                }
            }
        }

        ObjectAnimator animationX = ObjectAnimator.ofFloat(gameBall, "x", speedMoveX);
        animationX.setDuration(4000);

        ObjectAnimator animationY = ObjectAnimator.ofFloat(gameBall, "y", speedMoveY);
        animationY.setDuration(4000);

        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(animationX, animationY);
        animator.start();
        gameBall.getLocationOnScreen(ballLocation);
    }


    @Override
    public void onSensorChanged(SensorEvent event){
        TextView valShow = (TextView)findViewById(R.id.val);
        String stringValY = String.valueOf(event.values[2]);
        String stringValX = String.valueOf(event.values[0]);
        String stringValZ = String.valueOf(event.values[1]);
        valShow.setText("Y: " + stringValY + "    X: " + stringValX + "    Z: " + stringValZ);

        speedX = event.values[0];
        speedY = event.values[1];

        moveBall(speedX, speedY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}