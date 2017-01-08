package com.ashishgoel.got.requests;

import android.content.Context;

import com.android.volley.Request;
import com.ashishgoel.got.application.AppApplication;
import com.ashishgoel.got.serverApi.AppRequestListener;
import com.ashishgoel.got.serverApi.CustomStringRequest;

/**
 * Created by Ashish on 28/10/16.
 */

public class HomeAppRequests extends AppRequests {

    public static void makeHomeApiRequest(String url, AppRequestListener requestListener, Context context) {
        CustomStringRequest request = new CustomStringRequest(Request.Method.GET, url, HOME_REQUEST,
                requestListener, null, getHeaders(context));
        AppApplication.getInstance().addToRequestQueue(request, HOME_REQUEST);
    }
}
