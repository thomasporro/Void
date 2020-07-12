package com.thomasporro.avoid;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private final int MILLIS = 1000;
    private final int VIBRATION = 50;
    private final static String TAG = "MainActivity";

    private RelativeLayout relativeLayout;
    private TextView message;
    private int counter = 0;
    private Vibrator vibration;
    private long firstBackPressed;
    private Toast beforeExit;
    private SharedPreferences sharedPreferences;
    private CheckCode checkCode;


    private boolean backgroundChanged;
    private boolean scaledUp;
    private boolean scaledDown;

    private final String CHANGE_COLOR = "change_color";
    private final String SCALE_DOWN = "scale_down";
    private final String SCALE_UP = "scale_up";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hides the system UI
        hideSystemUI();

        //set up the layout and the variables of the view
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.background);
        message = findViewById(R.id.message);
        checkCode = new CheckCode();

        //access to the vibration service
        vibration = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //retrieve all information about the states of the game
        sharedPreferences = getSharedPreferences("VOID", MODE_PRIVATE);
        backgroundChanged = sharedPreferences.getBoolean(CHANGE_COLOR, false);
        scaledUp = sharedPreferences.getBoolean(SCALE_UP, false);
        scaledDown = sharedPreferences.getBoolean(SCALE_DOWN, false);

        //perform transformation of the game
        if(backgroundChanged){
            changeBackground(backgroundChanged);
        }
        scaleMessage(scaledUp, scaledDown);
    }


    @Override
    protected void onResume() {
        hideSystemUI();
        super.onResume();

    }

    @Override
    protected void onPause() {
        if(beforeExit != null) {
            beforeExit.cancel();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SCALE_UP, scaledUp);
        editor.putBoolean(SCALE_DOWN, scaledDown);
        editor.putBoolean(CHANGE_COLOR, backgroundChanged);
        editor.apply();
        super.onPause();
    }

    /**
     * Method that hides all the System UI
     */
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * Method called when a part of the screen is clicked, perform a different action for each
     * part of the screen
     * @param view the view clicked
     */
    //TODO improve all this code
    public void onClick(View view){
        String digit = (String) view.getTag();
        checkCode.addDigit(digit);
        counter++;
        //total_click +=1 ;
        
        if(counter == 6) {
            Actions guessed = checkCode.tryGuess();
            switch (guessed){
                case CHANGE_COLOR:
                    changeBackground(!backgroundChanged);
                    break;

                case SCALE_DOWN:
                    message.animate().scaleY(0.5f);
                    message.animate().scaleX(0.5f);
                    scaledUp = false;
                    scaledDown = true;
                    break;

                case SCALE_UP:
                    message.animate().scaleY(2);
                    message.animate().scaleX(2);
                    scaledUp = true;
                    scaledDown = false;
                    break;

                case RETURN_NORMAL:
                    message.animate().scaleY(1);
                    message.animate().scaleX(1);
                    scaledUp = false;
                    scaledDown = false;
                    break;
            }
            vibration.vibrate(VIBRATION);
            counter = 0;
            checkCode.clearHistory();
        }
    }

    /**
     * Method that change the color of the background and of the written text, black to white or
     * white to black
     *
     * @param alreadyChanged decides which transformation to perform, false to perform black to
     *                       white
     */
    private void changeBackground(boolean alreadyChanged){
        ValueAnimator anim = new ValueAnimator();
        TransitionDrawable transition = (TransitionDrawable) relativeLayout.getBackground();
        if(alreadyChanged){

            anim.setIntValues(Color.BLACK, Color.WHITE);
            anim.setEvaluator(new ArgbEvaluator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    message.setTextColor((Integer)valueAnimator.getAnimatedValue());
                }
            });
            anim.setDuration(100);
            anim.start();

            transition.startTransition(100);
            backgroundChanged = true;
        }else {
            anim.setIntValues(Color.WHITE, Color.BLACK);
            anim.setEvaluator(new ArgbEvaluator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    message.setTextColor((Integer)valueAnimator.getAnimatedValue());
                }
            });
            anim.setDuration(100);
            anim.start();

            transition.reverseTransition(100);

            backgroundChanged = false;
        }
    }

    /**
     * Scales the dimensions of the written text
     * @param scaledUp true if we want to scale up
     * @param scaledDown true if we want to scale down
     */
    private void scaleMessage(boolean scaledUp, boolean scaledDown){
        if(scaledUp){
            message.setScaleX(2);
            message.setScaleY(2);
        } else if (scaledDown){
            message.setScaleX(0.5f);
            message.setScaleY(0.5f);
        }
    }

    @Override
    public void onBackPressed() {
        if(firstBackPressed + MILLIS > System.currentTimeMillis()){
            beforeExit.cancel();
            super.onBackPressed();
        } else {
            beforeExit = Toast.makeText(getApplicationContext(), "Wait! That's illegal.",
                    Toast.LENGTH_LONG);
            beforeExit.show();
        }
        firstBackPressed = System.currentTimeMillis();
    }
}
