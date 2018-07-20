package com.arihanteducationgroup.online.indore.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.notification.Config;
import com.arihanteducationgroup.online.indore.notification.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import io.fabric.sdk.android.Fabric;

import static com.arihanteducationgroup.online.indore.Constants.CheckInternetConnection.checkLocationPermission;
import static com.arihanteducationgroup.online.indore.other.MySharedPref.getData;

public class SplashActivity extends BaseActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = Main2Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        boolean per =  checkLocationPermission(SplashActivity.this);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//
               displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };


        long SPLASH_TIME_OUT=3000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                boolean islogin;
                islogin=getData(SplashActivity.this,"login",false);
                if (islogin)
                {
                    Intent i = new Intent(SplashActivity.this, Main2Activity.class);
                    startActivity(i);
                    finish();
                    return;
                }
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();



            }
        }, SPLASH_TIME_OUT);

    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
       String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            System.err.println("====== firebaseRegId=========" + regId);
        else
            System.err.println("====== firebaseRegId not registered =========" + regId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
