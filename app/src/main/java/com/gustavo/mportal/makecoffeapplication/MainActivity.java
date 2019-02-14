package com.gustavo.mportal.makecoffeapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

/*
    Comentário
 */

public class MainActivity extends AppCompatActivity {

    private WebView wb;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wb = (WebView) findViewById(R.id.webview);
        wb.setWebChromeClient(new WebChromeClient());
        webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wb.loadUrl("file:///android_asset/random_coffe.html");
    }
}
