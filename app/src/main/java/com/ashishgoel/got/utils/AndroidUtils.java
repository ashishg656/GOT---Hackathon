package com.ashishgoel.got.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;

/**
 * Created by Ashish on 09/07/16.
 */
public class AndroidUtils {

    public static boolean compareString(String str1, String str2) {
        return compareString(str1, str2, true);
    }

    public static boolean compareString(String str1, String str2, boolean logError) {
        try {
            if (str1.equalsIgnoreCase(str2)) {
                return true;
            }
        } catch (Exception e) {
            if (logError) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isEmpty(String text) {
        try {
            if (text != null && text.length() > 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isEmpty(EditText editText) {
        try {
            if (editText != null && editText.getText().toString().trim().length() > 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean checkEmailFormat(String email) {
        try {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDeviceId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = telephonyManager.getDeviceId();
            if (deviceId == null || deviceId.length() == 0) {
                return getDeviceIdUsingContentResolver(context);
            } else {
                return deviceId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            DebugUtils.logException(e, true);
            return getDeviceIdUsingContentResolver(context);
        }
    }

    private static String getDeviceIdUsingContentResolver(Context context) {
        try {
            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
            DebugUtils.logException(e, true);
        }
        return "";
    }

    public static String getConnectivityStatus(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    boolean isWiFi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                    if (isWiFi) {
                        return "WIFI";
                    }
                }
            }
        } catch (Exception e) {
            DebugUtils.logException(e, true);
        }
        return getConnectionTypeSim(context);
    }

    private static String getConnectionTypeSim(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned getHtmlText(String source) {
        try {
            Spanned result;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
            } else {
                result = Html.fromHtml(source);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static String convertFromSpannedToHtml(Spanned source) {
        try {
            String result;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.toHtml(source, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
            } else {
                result = Html.toHtml(source);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
