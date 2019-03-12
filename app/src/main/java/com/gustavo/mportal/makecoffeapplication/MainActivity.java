package com.gustavo.mportal.makecoffeapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JacksonUtils;
import com.gustavo.mportal.makecoffeapplication.model.JsonNotificacao;
import com.gustavo.mportal.makecoffeapplication.model.Notificacao;
import com.gustavo.mportal.makecoffeapplication.request.RequestNetworkModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        String json = null;
        params = new HashMap<>();
        //Montando header
        params.put("Content-Type","application/json");
        params.put("Authorization","key=AAAArIa51ng:APA91bEaFtrIf-1mD6i636PUI-LPgTTiXLhxsilhQZ8QuLJFJ0DefuCNjrYbqLDI2gqGyi9ZI1QZ5vii82tErxw8gOrAcV6TuAGd-8L3QMkeDm0fNcGgpZFO3kZEFZrO8Vpk3vdxVVZa");

        //Montando Body
        Notificacao notificacao = new Notificacao("Make Coffee","Requisicao feito via codigo");
        JsonNotificacao jsonNotificacao = new JsonNotificacao("cMt5_TXfvec:APA91bETEwj4nE6pe2dUcoMkLTGzJQRw-xmwsVTsODGah4IJusEI1Po70mTTlAZxBv_KDyHqcy2XYOJwv-lh7-FDb1bZLUUSuEZguAUoXu4WkDaAxL1D9VJpJoUHSICjsh90eNqOdITL"
            , notificacao);

        //Convertendo java para json
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(jsonNotificacao);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //Montando requisicao
        final String requestBody = json;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onResponse",error.getMessage());
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
            return params;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        //Fazendo requisicao
        requestQueue.add(request);












        /*RequestNetworkModel requestNetworkModel = new RequestNetworkModel(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                params, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.i(TAG, response.data.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //Toast.makeText(getApplicationContext(),"Erro Http: " + error.networkResponse.statusCode,Toast.LENGTH_SHORT).show();
              Toast.makeText(getApplicationContext(),"Sem choro por favor ",Toast.LENGTH_SHORT).show();
            }
        });


        requestNetworkModel.setTag("tag");
        requestQueue.add(requestNetworkModel);*/

    }

}
