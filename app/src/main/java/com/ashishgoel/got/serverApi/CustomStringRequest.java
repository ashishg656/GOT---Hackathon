package com.ashishgoel.got.serverApi;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ashishgoel.got.utils.DebugUtils;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class CustomStringRequest extends StringRequest {

    AppRequestListener appRequestListener;
    HashMap<String, String> params;
    HashMap<String, String> headers;
    String tag;
    String url;

    JSONObject jsonObject;

    long requestStartTime;

    public CustomStringRequest(int method, String url, String tag,
                               AppRequestListener appRequestListener,
                               HashMap<String, String> params, HashMap<String, String> headers) {
        this(method, url, tag, appRequestListener, params, headers, null);
    }

    public CustomStringRequest(int method, String url, String tag,
                               AppRequestListener appRequestListener,
                               HashMap<String, String> params, HashMap<String, String> headers, JSONObject jsonObject) {
        super(method, url, null, null);
        this.appRequestListener = appRequestListener;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        this.url = url;
        this.jsonObject = jsonObject;

        DebugUtils.logRequests("Request Started. URL = " + url);

        if (DebugUtils.noteAppRequestTimes) {
            requestStartTime = System.currentTimeMillis();
        }

        if (appRequestListener != null) {
            appRequestListener.onRequestStarted(tag);
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (jsonObject == null) {
            return super.getBody();
        } else {
            try {
                DebugUtils.log(jsonObject.toString());
                return jsonObject.toString().getBytes("UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                return super.getBody();
            }
        }
    }

    @Override
    protected void deliverResponse(String response) {
        if (DebugUtils.noteAppRequestTimes) {
            long requestEndTime = System.currentTimeMillis();
            long difference = (requestEndTime - requestStartTime);
            DebugUtils.logRequests("Request Complete in " + difference + "ms. URL = " + url);
        } else {
            DebugUtils.logRequests("Request Complete. URL = " + url);
        }
        DebugUtils.printToSystem(response);

        sendResponseToListener(response);
    }

    private void sendResponseToListener(String response) {
        if (appRequestListener != null) {
            appRequestListener.onRequestCompleted(tag, response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (DebugUtils.noteAppRequestTimes) {
            long requestEndTime = System.currentTimeMillis();
            long difference = (requestEndTime - requestStartTime);
            DebugUtils.logRequests("Request Failed in " + difference + "ms. URL = " + url);
        } else {
            DebugUtils.logRequests("Request Failed. URL = " + url);
        }

        DebugUtils.printToSystem(error);

        if (appRequestListener != null) {
            appRequestListener.onRequestFailed(tag, error, false);
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : Collections.<String, String>emptyMap();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : Collections.<String, String>emptyMap();
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }
}
