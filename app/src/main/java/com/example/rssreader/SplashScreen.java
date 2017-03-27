package com.example.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by MrJohnnyReds on 22/03/2017.
 */

public class SplashScreen extends Activity {
    // Splash screen timer
    private static int TIME_OUT = 3000;

    //trying new progress bar
    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);
        //mProgress= (ProgressBar) findViewById(R.id.progressBar);
        //make the progress bar visible
        //mProgress.setVisibility(View.VISIBLE);
        //mProgress.setMax(150);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }
}
