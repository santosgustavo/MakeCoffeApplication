package com.gustavo.mportal.makecoffeapplication.request;

import android.net.sip.SipSession;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class RequestNetworkModel extends Request<JSONObject> {

    private Response.Listener<JSONObject> response;
    private Map<String, String> params;


    public RequestNetworkModel(int method, String url, Map<String,String> params, Response.Listener<NetworkResponse> response, Response.ErrorListener errorListener){
        super(method,url,errorListener);

    }

    public Map<String,String> getParams () throws AuthFailureError{
        return params;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {
            String js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return(Response.success(new JSONObject(js),HttpHeaderParser.parseCacheHeaders(response)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        this.response.onResponse(response);
    }
}
