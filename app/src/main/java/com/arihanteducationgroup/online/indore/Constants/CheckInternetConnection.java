package com.arihanteducationgroup.online.indore.Constants;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.arihanteducationgroup.online.indore.R;

public class CheckInternetConnection extends Activity {
    public static final int MY_PERMISSIONS_CAMERA = 125;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 124;
    public static final int MY_PERMISSIONS_SMS = 126;
    public static final int MY_FINE_LOCATION = 121;
    public static final int MY_COARSE_LOCATION = 120;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;

    static boolean grantLOc;
    static String provider;
    private static LocationManager locationManager;

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager

                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean checkLocationPermission(final Activity context) {


        System.out.println("--- check location permission ----------");
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(context)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(context,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkReadStoragePermission(final Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.READ_EXTERNAL_STORAGE")) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("External storage permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    public static boolean checkWriteStoragePermission(final Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Storage permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        return false;
    }

    public static boolean checkCameraPermission(final Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.CAMERA") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.CAMERA")) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Camera permission is necessary");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.CAMERA"}, MY_PERMISSIONS_CAMERA);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.CAMERA"}, MY_PERMISSIONS_CAMERA);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


}
