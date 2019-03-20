package com.gustavo.mportal.makecoffeapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.mportal.makecoffeapplication.model.JsonNotificacao;
import com.gustavo.mportal.makecoffeapplication.model.Notificacao;
import com.gustavo.mportal.makecoffeapplication.model.NotificacaoTokenAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private List<String> tokenList;
    String mensagem;

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
    public void enviarNotificacao(String mensagem){
        tokenList = new ArrayList<>();
        this.mensagem = mensagem;
        obterListaDeToken();
    }

    public void fazerRequisicaoFirebase(List<String> tokens,String mensagem){
        if(!tokens.isEmpty()) {
            for (int i = 0; i < tokens.size(); i++) {
                String json = null;
                params = new HashMap<>();
                //Montando header
                params.put("Content-Type", "application/json");
                params.put("Authorization", "key=AAAArIa51ng:APA91bEaFtrIf-1mD6i636PUI-LPgTTiXLhxsilhQZ8QuLJFJ0DefuCNjrYbqLDI2gqGyi9ZI1QZ5vii82tErxw8gOrAcV6TuAGd-8L3QMkeDm0fNcGgpZFO3kZEFZrO8Vpk3vdxVVZa");

                //Montando Body
                Notificacao notificacao = new Notificacao("Make Coffee", trataMensagemNotificacao(mensagem));
                JsonNotificacao jsonNotificacao = new JsonNotificacao(tokens.get(i), notificacao);

                //Convertendo objeto java para json
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
                        Log.i(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.getMessage());
                    }
                }) {
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
            }
        }else {
            Log.i(TAG,"Lista de token vazia");
        }
    }


    public JSONArray obterListaDeToken(){
        final List<NotificacaoTokenAPI> list = new ArrayList<>();
        final JSONArray array = new JSONArray();

        //Montando requisicao
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "https://robo-notificacao.herokuapp.com/notificacoes/findAll", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "Sucesso na requisicao GET /notificacoes/findAll");
                try {
                    for (int i =0; i < response.length(); i++) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        NotificacaoTokenAPI notificacaoToken = objectMapper.readValue(response.get(i).toString(), NotificacaoTokenAPI.class);
                        tokenList.add(notificacaoToken.getToken_id());
                        Log.i(TAG, "Token adicionado na lista: " + notificacaoToken.getToken_id());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fazerRequisicaoFirebase(tokenList,mensagem);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getMessage() != null)
                    Log.i(TAG,error.getMessage());
            }
        });

        //Fazendo requisicao
        requestQueue.add(request);

        return array;
    }



    public String trataMensagemNotificacao(String mensagem){
        String[] stringSplit = mensagem.split("\\.");

        return stringSplit[0] + " foi sorteado no app.";
    }

}
