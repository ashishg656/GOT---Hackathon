package com.ashishgoel.got.activity;

import android.os.Bundle;

import com.ashishgoel.got.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ashish on 08/01/17.
 */

public class SplashScreenActivity extends BaseActivity {

    int splashDuration = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_layout);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                openHomeActivity();
                finish();
            }
        }, splashDuration);
    }
}
