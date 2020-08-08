package com.example.accelerometertest;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
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
    private Rect[] Holes;
    private int[] ballLocation = {0, 0};
    private float speedX;
    private float speedY;
    private  TextView valshow;
    private Toast testToast;


    @Override
    //creates game
    protected void onCreate(Bundle savedInstanceState) {
        //setting up game
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up sensors, sensor manager, and registering sensor listener
        myManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        myAcc = myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myManager.registerListener(this, myAcc, SensorManager.SENSOR_DELAY_NORMAL);

        valshow = findViewById(R.id.val);

        //setting up ball
        gameBall = (ImageView)findViewById(R.id.ball);

        //setting up array of walls/individual walls and holes
        Holes = new Rect[1];
        ImageView Hole = (ImageView)findViewById(R.id.hole);
        valshow.setText(Hole.toString());
        Holes[0] = new Rect(Hole.getLeft(), Hole.getTop(), Hole.getRight(), Hole.getBottom());
        Walls = new ImageView[1];
        ImageView wall = (ImageView)findViewById(R.id.wall);
        Walls[0] = wall;
        testToast = Toast.makeText(this, "test", Toast.LENGTH_SHORT);
    }


    //determines when and how fast ball moves and makes image of ball move on screen
    public void moveBall(float valX, float valY){

        //determine speed based on accelerometer values in X and Y direction
        float speedMoveX = (float)(-571.4 * valX);
        float speedMoveY = (float)(571.4 * valY);

        //Moving right (positive)
        if(speedMoveX > 0){
            for(int i = 0; i < Walls.length; i++){
                if(Walls[i].getRight() >= (ballLocation[0] + gameBall.getWidth())
                    && Walls[i].getLeft() < (ballLocation[0] + gameBall.getWidth())){
                    if(((Walls[i].getTop() < (ballLocation[1] - gameBall.getHeight())) && Walls[i].getBottom() > ballLocation[1]) ||
                            (ballLocation[1] < Walls[i].getBottom() && ballLocation[1] > Walls[i].getTop()) ||
                            ((ballLocation[1] - gameBall.getHeight()) < Walls[i].getBottom() &&  (ballLocation[1] - gameBall.getHeight()) > Walls[i].getTop()))
                        speedMoveX = 0;

                }
            }
        }

        //Moving left (negative)
        else{
            for(int i = 0; i < Walls.length; i++){
                if(Walls[i].getRight() <= (ballLocation[0])
                        && Walls[i].getLeft() > (ballLocation[0])) {
                    if(((Walls[i].getTop() < (ballLocation[1] - gameBall.getHeight())) && Walls[i].getBottom() > ballLocation[1]) ||
                            (ballLocation[1] < Walls[i].getBottom() && ballLocation[1] > Walls[i].getTop()) ||
                            ((ballLocation[1] - gameBall.getHeight()) < Walls[i].getBottom() &&  (ballLocation[1] - gameBall.getHeight()) > Walls[i].getTop()))
                        testToast.show();
                }
            }
        }

        //if ball is moving in positive Y-direction(downward), checks ball is not touching top, bottom,
        //right or left edges of any of the walls. If it is, sets speedMoveY to zero.
        if(speedMoveY > 0){
            for(int i = 0; i < Walls.length; i++){
                if(Walls[i].getTop() <= (ballLocation[1] - 75)
                        && Walls[0].getBottom() > (ballLocation[1] - 75))
               {
                    if((Walls[i].getLeft() <= ((ballLocation[0] + gameBall.getWidth() - 25)))
                            && ((Walls[i].getRight() > ((ballLocation[0] - gameBall.getWidth() + 213)))))
                        speedMoveY = 0;
                }
            }
        }
        //if ball is moving in negative Y-direction(upward), checks ball is not touching top, bottom,
        //right or left edges of any of the walls. If it is, sets speedMoveY to zero.
        else{
            for(int i = 0; i < Walls.length; i++){
                if(Walls[i].getBottom() >= (ballLocation[1] - (gameBall.getHeight()*1.5))
                    && Walls[i].getTop() < (ballLocation[1] - (gameBall.getHeight()*1.5))) {
                    if((Walls[i].getLeft() <= ((ballLocation[0] + gameBall.getWidth())))
                            && ((Walls[i].getRight() > ((ballLocation[0] - gameBall.getWidth()) + 213))))
                        speedMoveY = 500;

                }
            }
        }

        //sets up animation of ball in X direction
        ObjectAnimator animationX = ObjectAnimator.ofFloat(gameBall, "x", speedMoveX);
        animationX.setDuration(4000);

        //sets up animation of ball in X direction
        ObjectAnimator animationY = ObjectAnimator.ofFloat(gameBall, "y", speedMoveY);
        animationY.setDuration(4000);

        //plays X and Y animation of ball
        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(animationX, animationY);
        animator.start();

        //Define ball rect
        Rect BallRect = new Rect(ballLocation[0], ballLocation[1], (ballLocation[0] + gameBall.getWidth()), (ballLocation[1] - gameBall.getHeight()));

        //valshow.setText(Holes[0].toString());
        //Check if the ball fell in a hole
        for(int i = 0; i < Holes.length; i++){
            if(Holes[i].intersect(BallRect)) {
                testToast.show();
                gameBall.setX(0);
                gameBall.setY(0);
            }
        }

        //gets the location of the ball image on the screen and stores it in the ballLocation variable
        gameBall.getLocationOnScreen(ballLocation);
    }


    @Override
    /*if the tilt of the phone changes (which will change the accelerometer values), gets the
      X-, Y-, and Z-Values from the accelerometer and passes them to the moveBall function*/
    public void onSensorChanged(SensorEvent event){

        //passes the X- and Y-Value to moveBall
        speedX = event.values[0];
        speedY = event.values[1];
        moveBall(speedX, speedY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}