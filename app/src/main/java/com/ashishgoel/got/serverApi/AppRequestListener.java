package com.ashishgoel.got.serverApi;

import com.android.volley.VolleyError;

public interface AppRequestListener {

    public void onRequestStarted(String requestTag);

    public void onRequestFailed(String requestTag, VolleyError error, boolean networkError);

    public void onRequestCompleted(String requestTag, String response);
}
