package com.arihanteducationgroup.online.indore.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arihanteducationgroup.online.indore.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.arihanteducationgroup.online.indore.Constants.CheckInternetConnection.checkWriteStoragePermission;

public class DetailVacancy extends BaseActivity {

    TextView tv_loading;
    String dest_file_path = "ArihantEducationGroup.pdf";
    int downloadedSize = 0, totalsize;
    float per = 0;
    private String pdfUrl = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vacancy);
        TextView headerTv = findViewById(R.id.header_text);
        TextView downloadTV = findViewById(R.id.downloadTV);
         tv_loading = findViewById(R.id.tv_loading);
        TextView dateTV = findViewById(R.id.dateTV);
        TextView descTV = findViewById(R.id.descTV);
        ImageView back_btn = findViewById(R.id.back_btn);
        ImageView iconIV = findViewById(R.id.iconIV);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString("vacancies_title");
            String vacancies_icon_img = bundle.getString("vacancies_icon_img", "");
            String vacancies_desc = bundle.getString("vacancies_desc");
            String date = bundle.getString("date", "");
            pdfUrl = bundle.getString("PDF", "");
            headerTv.setText(title);

            if (date.length() > 0) {
                dateTV.setVisibility(View.VISIBLE);
                dateTV.setText(date);
            } else
                dateTV.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                descTV.setText(Html.fromHtml(vacancies_desc, Html.FROM_HTML_MODE_COMPACT));

            else
                descTV.setText(Html.fromHtml(vacancies_desc));

            if (vacancies_icon_img.length() > 0)
                Glide.with(getApplicationContext()).load(vacancies_icon_img).apply(RequestOptions
                        .placeholderOf(R.drawable.logo_icon)).into(iconIV);

            else
                iconIV.setVisibility(View.GONE);


        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        if (pdfUrl.length() != 0) {
            downloadTV.setVisibility(View.VISIBLE);
            tv_loading.setVisibility(View.VISIBLE);
        }
        else
        {
            downloadTV.setVisibility(View.GONE);
            tv_loading.setVisibility(View.GONE);
        }

        if (downloadTV.getVisibility() == View.VISIBLE) {
            downloadTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.err.println("---- pdf url ---- ");
                    System.err.println(pdfUrl);
                    boolean permission = checkWriteStoragePermission(DetailVacancy.this);
                    if (permission)
                    downloadAndOpenPDF(pdfUrl);

                }
            });
        }

    }


    void downloadAndOpenPDF(final String download_file_url) {
        new Thread(new Runnable() {
            public void run() {
                Uri path = Uri.fromFile(downloadFile(download_file_url));
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } catch (ActivityNotFoundException e) {
                    tv_loading
                            .setError("PDF Reader application is not installed in your device");
                }
            }
        }).start();

    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();
            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {
            setTextError("Some error occurred. Press back and try again.",
                    Color.RED);
        } catch (final IOException e) {
            setTextError("Some error occured. Press back and try again.",
                    Color.RED);
        } catch (final Exception e) {
            setTextError(
                    "Failed to download image. Please check your internet connection.",
                    Color.RED);
        }
        return file;
    }

    void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setTextColor(color);
                tv_loading.setText(message);
            }
        });

    }

    void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });

    }

}
