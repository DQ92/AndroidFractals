package com.example.daniel.fraktaleandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends Activity {

    private static  int SPLASH_SCREEN_DELAY = 2500;
    private final static String TAG_INFO = "Errors";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Log.d(TAG_INFO, "< -------- onCreate ------- > " + getLocalClassName());

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG_INFO,"onResume "+getClass().toString());

        launchActivity();
    }


    /**
     * Start MainActivity with tabs
     */
    public void launchActivity() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_DELAY);


    }

}