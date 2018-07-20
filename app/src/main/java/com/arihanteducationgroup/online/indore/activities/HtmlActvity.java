package com.arihanteducationgroup.online.indore.activities;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.arihanteducationgroup.online.indore.R;
import com.arihanteducationgroup.online.indore.other.GifImageView;

public class HtmlActvity extends BaseActivity {
    WebView webView;
    GifImageView progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_aboutus);

        TextView headerTv =findViewById(R.id.header_text);
        WebView webView = (WebView)findViewById(R.id.webview);
        ImageView back_btn =findViewById(R.id.back_btn);
         progressBar = findViewById(R.id.loaderGif);
        progressBar.setGifImageResource(R.drawable.books);
        Bundle bundle =getIntent().getExtras();
        if (bundle !=null)
        {
            String header =bundle.getString("header_name");
            headerTv.setText(header);
            webView.setWebViewClient(new myWebClient());

            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            WebSettings settings = webView.getSettings();
            settings.setLoadWithOverviewMode(true);

            settings.setJavaScriptEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setDomStorageEnabled(true);

            assert header != null;
            if (header.equalsIgnoreCase("About Us"))
                webView.loadUrl("http://www.arihanteducationgroup.com/arihant-html/about.php");

           else if (header.equalsIgnoreCase("Online Exam"))
               webView.loadUrl("http://mock.arihanteducationgroup.com/take_exam");
           else
                webView.loadUrl("http://arihanteducationgroup.com/arihant-html/founder.html");

        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }


}
