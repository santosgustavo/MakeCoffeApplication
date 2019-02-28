package com.gustavo.mportal.makecoffeapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.gustavo.mportal.makecoffeapplication.request.RequestNetworkModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
    Comentário
 */

public class MainActivity extends AppCompatActivity {

    private WebView wb;
    private WebSettings webSettings;
    private Map<String,String> params;
    private RequestQueue requestQueue;
    private String TAG = "MainActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wb = (WebView) findViewById(R.id.webview);
        wb.addJavascriptInterface(this ,"android");
        wb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wb.loadUrl("file:///android_asset/random_coffe.html");
        //wb.loadUrl("http://multiportal.1gps.com.br/apps/multiportal/login.seam");

        requestQueue = new Volley().newRequestQueue(MainActivity.this);
    }

    @JavascriptInterface
    public void toast(String mensagem){

        params = new HashMap<>();
        params.put("Content-Type","application/json");
        params.put("Authorization","key=AAAArIa51ng:APA91bEaFtrIf-1mD6i636PUI-LPgTTiXLhxsilhQZ8QuLJFJ0DefuCNjrYbqLDI2gqGyi9ZI1QZ5vii82tErxw8gOrAcV6TuAGd-8L3QMkeDm0fNcGgpZFO3kZEFZrO8Vpk3vdxVVZa");



        RequestNetworkModel requestNetworkModel = new RequestNetworkModel(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                params, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.i(TAG, response.data.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Toast.makeText(getApplicationContext(),"Erro Http: " + error.networkResponse.statusCode,Toast.LENGTH_SHORT).show();
            }
        });


        requestNetworkModel.setTag("tag");
        requestQueue.add(requestNetworkModel);

    }

}
