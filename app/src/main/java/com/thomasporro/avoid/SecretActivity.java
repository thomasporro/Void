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
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.tasks.Task;

public class SecretActivity extends AppCompatActivity {

    final String TAG = "SecretActivity";

    private int counter = 0;
    private RelativeLayout relativeLayout;
    private TextView message;
    private final String CHANGE_COLOR = "change_color";
    private final int RC_SIGN_IN = 1;
    private GoogleSignInAccount mGoogleSignInAccount;


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
        signIn();

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

    /**
     * Performs the sign in into a google account
     */
    void signIn(){
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        //Performs the login into the google account
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                Log.d(TAG, "Successfully connected with Google Play Services");
                mGoogleSignInAccount = result.getSignInAccount();
                GamesClient gamesClient = Games.getGamesClient(this, mGoogleSignInAccount);
                gamesClient.setViewForPopups(findViewById(android.R.id.content));
                Games.getAchievementsClient(this, mGoogleSignInAccount)
                        .unlock(getString(R.string.you_did_it));
            } else {
                Log.e(TAG, "Not connected with Google Play Services");
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = "hai sbagliato";
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }
}
