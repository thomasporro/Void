package com.thomasporro.avoid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private final int MILLIS = 1000;
    private final int VIBRATION = 50;
    private RelativeLayout relativeLayout;
    private TextView message;
    private int counter = 0;
    private boolean backgroundChanged = false;
    private String momentaneous = "";
    private Vibrator v;
    private long firstBackPressed;
    private Toast beforeExit;

    private String[] stringAccepted = {"abcdef"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.background);
        message = findViewById(R.id.message);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
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

    //TODO improve all this code
    public void onClick(View view){
        String tag = (String) view.getTag();
        boolean resolved = false;
        momentaneous += tag;
        counter++;

        if(counter == 6) {
            for(String accepted : stringAccepted){
                if(accepted.equals(momentaneous)){
                    if(!backgroundChanged){
                        relativeLayout.setBackgroundResource(R.color.black);
                        message.setTextColor(getResources().getColor(R.color.white));
                        backgroundChanged = true;
                    } else {
                        relativeLayout.setBackgroundResource(R.color.white);
                        message.setTextColor(getResources().getColor(R.color.black));
                        backgroundChanged = false;
                    }
                    resolved = true;
                }
            }
            if(!resolved){
                v.vibrate(VIBRATION);
            }
            momentaneous = "";
            counter = 0;
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
