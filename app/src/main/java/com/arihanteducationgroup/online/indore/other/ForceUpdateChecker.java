package com.arihanteducationgroup.online.indore.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.arihanteducationgroup.online.indore.activities.SplashActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;


import java.io.IOException;

public  class ForceUpdateChecker extends AsyncTask {

    private String latestVersion;
    private String currentVersion;
    private Context context;
    public ForceUpdateChecker(String currentVersion, Context context){
        this.currentVersion = currentVersion;
        this.context = context;
    }

    public void showForceUpdateDialog(){

        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName()+ "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(3) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();
            Log.e("latestversion","---"+latestVersion);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    protected void onPostExecute(Object o) {
        if(latestVersion!=null){
            if(!currentVersion.equalsIgnoreCase(latestVersion)){
                // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                if(!(context instanceof SplashActivity)) {
                    if(!((Activity)context).isFinishing()){
                        showForceUpdateDialog();
                    }
                }
            }
        }
        super.onPostExecute(o);
    }
}