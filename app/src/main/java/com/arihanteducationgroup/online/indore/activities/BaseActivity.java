package com.arihanteducationgroup.online.indore.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.arihanteducationgroup.online.indore.Constants.ConnectivityReceiver;
import com.arihanteducationgroup.online.indore.Constants.MyApplication;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.other.ForceUpdateChecker;


import java.util.Objects;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);

        forceUpdate();
    }

    private void showSnack(boolean isConnected) {
        System.out.println("------- is internet available------- ");
        System.out.println(isConnected);

        if (!isConnected ) {
            Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Connect to internet", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }

    private void forceUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateChecker(currentVersion,BaseActivity.this).execute();
    }

}
