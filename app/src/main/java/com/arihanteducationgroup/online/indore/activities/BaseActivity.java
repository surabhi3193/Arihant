package com.arihanteducationgroup.online.indore.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.arihanteducationgroup.online.indore.Constants.ConnectivityReceiver;
import com.arihanteducationgroup.online.indore.Constants.App;
import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.other.ForceUpdateChecker;


import java.util.Objects;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, ForceUpdateChecker.OnUpdateNeededListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().setConnectivityListener(this);

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


     void openDialog(final  String url) {
        final Dialog dialog = new Dialog(BaseActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
         dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                 WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        TextView ok_btn = dialog.findViewById(R.id.ok_btn);
        WebView webView = dialog.findViewById(R.id.webView);
         WebSettings settings = webView.getSettings();

         settings.setLoadWithOverviewMode(true);

         settings.setJavaScriptEnabled(true);
         settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
         settings.setDomStorageEnabled(true);
         webView.loadUrl(url);

        webView.requestFocus();


        final ProgressDialog progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue .")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
