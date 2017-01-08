package com.ashishgoel.got.utils;

import com.android.volley.Cache;
import com.ashishgoel.got.application.AppApplication;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Ashish on 10/05/16.
 */
public class VolleyUtils {

    public static Object getResponseObject(String response, Class objectClass) {
        try {
            return new Gson().fromJson(response, objectClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getResponseObject(JSONObject response, Class objectClass) {
        try {
            return new Gson().fromJson(response.toString(), objectClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getResponseFromCache(Class objectClass, String url) throws Exception {
        try {
            Cache.Entry entry = AppApplication.getInstance()
                    .getRequestQueue().getCache().get(url);
            String data = new String(entry.data, "UTF-8");
            return new Gson().fromJson(data, objectClass);
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean checkIfUrlExistInCache(String url) {
        try {
            Cache.Entry entry = AppApplication.getInstance()
                    .getRequestQueue().getCache().get(url);
            String data = new String(entry.data, "UTF-8");
            if (!AndroidUtils.isEmpty(data)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getResponseFromCache(String url) throws Exception {
        try {
            Cache.Entry entry = AppApplication.getInstance()
                    .getRequestQueue().getCache().get(url);
            String data = new String(entry.data, "UTF-8");
            return data;
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getStringFromObject(Object obj) {
        try {
            return new Gson().toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
                Type listType = new TypeToken<ArrayList<Influencer>>() {
                }.getType();
                List<Influencer> mData = new Gson().fromJson(response, listType);
     */
}
