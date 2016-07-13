package com.fiume.billingmechine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.fiume.billingmechine.R;

/**
 * Created by Razi on 12/8/2015.
 */
public class ActivitySplash extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                /*// This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences prefs = getSharedPreferences("RICHIEBULLDATA", MODE_PRIVATE);
                if (prefs.getInt("status", 0) == 1) {*/
                Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                ActivitySplash.this.finish();


                //finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
