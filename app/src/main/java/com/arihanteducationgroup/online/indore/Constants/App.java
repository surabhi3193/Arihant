package com.arihanteducationgroup.online.indore.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arihanteducationgroup.online.indore.other.ForceUpdateChecker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class App extends Application implements Application.ActivityLifecycleCallbacks, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = App.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static App mInstance;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        setUpdateFirebase();
        registerActivityLifecycleCallbacks(this);
    }

    private void setUpdateFirebase() {

        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // set in-app defaults
        HashMap<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.7.4");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=com.arihanteducationgroup.online.indore");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        System.out.println("----------- app onActivityStarted--------------");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        System.out.println("----------- app onActivityResumed--------------");

    }


    @Override
    public void onActivityPaused(Activity activity) {

        System.out.println("----------- app onActivityPaused--------------");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        try {
            boolean foreground = new ForegroundCheckTask().execute(getApplicationContext()).get();
            if (!foreground) {
                //App is in Background - do what you want
                System.out.println("---------- app is in fore ground----------");
                System.runFinalization();
                Runtime.getRuntime().gc();
                System.gc();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        System.out.println("----------- app destroyed--------------");

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }

}
