package com.thomasporro.avoid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecretActivity extends AppCompatActivity {

    private int counter = 0;
    private RelativeLayout relativeLayout;
    private TextView message;
    private final String CHANGE_COLOR = "abcdef";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        hideSystemUI();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);
        message = findViewById(R.id.message);
        relativeLayout = findViewById(R.id.background);

        SharedPreferences sharedPreferences = getSharedPreferences("VOID", MODE_PRIVATE);
        boolean backgroundChanged = sharedPreferences.getBoolean(CHANGE_COLOR, false);
        changeBackground(backgroundChanged);
        //TODO unlock the achievemetn
    }

    public void onClick(View view){
        counter++;

        if(counter == 6) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
            finish();
        }
    }

    private void changeBackground(boolean alreadyChanged){
        if(alreadyChanged){
            TransitionDrawable transition = (TransitionDrawable) relativeLayout.getBackground();
            ValueAnimator anim = new ValueAnimator();
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
        }
    }


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
}
