package com.ashishgoel.got.requests;

import android.content.Context;

import com.ashishgoel.got.extras.RequestTags;

import java.util.HashMap;

/**
 * Created by Ashish on 04/06/16.
 */
public class AppRequests implements RequestTags {

    public static HashMap<String, String> getHeaders(Context context) {
        HashMap<String, String> headers = new HashMap<>();
        return headers;
    }
}
