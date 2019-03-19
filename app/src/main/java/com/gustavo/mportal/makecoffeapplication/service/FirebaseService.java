package com.gustavo.mportal.makecoffeapplication.service;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gustavo.mportal.makecoffeapplication.model.JsonNotificacao;
import com.gustavo.mportal.makecoffeapplication.model.Notificacao;
import com.gustavo.mportal.makecoffeapplication.model.NotificacaoTokenAPI;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FirebaseService extends FirebaseMessagingService {

    private String TAG = "FirebaseServiceLog";
    RequestQueue requestQueue;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    Log.i(TAG,"Passei aqui");

        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        Log.i(TAG,"Token: " + s);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        gravarTokenNoBanco(s);
        super.onNewToken(s);
    }

    public void gravarTokenNoBanco(String token){
        String json = null;
        final HashMap params = new HashMap<>();
        //Montando header
        params.put("Content-Type","application/json");
        params.put("login","admin");
        params.put("senha","senha");

        //Montando Body
        NotificacaoTokenAPI notificacaoToken = new NotificacaoTokenAPI("generico", token);

        //Convertendo objeto java para json
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(notificacaoToken);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //Montando requisicao
        final String requestBody = json;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://robo-notificacao.herokuapp.com/notificacoes/insertToken", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.getMessage() != null)
                Log.i(TAG,error.getMessage());
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
    }
}
