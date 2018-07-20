package com.arihanteducationgroup.online.indore.Constants;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, ConnectivityReceiver.ConnectivityReceiverListener {

    private static MyApplication mInstance;
    private Activity myactivity;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        registerActivityLifecycleCallbacks(this);
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

        myactivity = activity;

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
