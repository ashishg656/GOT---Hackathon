package com.ashishgoel.got.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Ashish on 11/05/16.
 */
public class UIUtils {

    public static void loadImage(ImageView imageView, int resource) {
        try {
            imageView.setImageResource(resource);
        } catch (Throwable r) {
            r.printStackTrace();
        }
    }

    public static int getDeviceHeight(Context context) {
        try {
            int height = context.getResources().getDisplayMetrics().heightPixels;
            return height;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDeviceWidth(Context context) {
        try {
            int width = context.getResources().getDisplayMetrics().widthPixels;
            return width;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void openAndroidSettingsScreen(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    public static void hideSoftKeyboard(Activity context) {
        try {
            View v = context.getWindow().getCurrentFocus();
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            } else {
                try {
                    // TODO handle hide keyboard when view is null
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSoftKeyboard(Activity context, EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlertDialog showDialog(Context context, AlertDialog alertDialog) {
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alertDialog;
    }

    public static AlertDialog hideDialog(Context context, AlertDialog alertDialog) {
        try {
            alertDialog.dismiss();
            alertDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alertDialog;
    }

    public static void showDialog(Context context, ProgressDialog alertDialog) {
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProgressDialog showProgressDialog(Context context, String title, String message, boolean cancelable) {
        try {
            return ProgressDialog.show(context, title, message, true, cancelable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideDialog(Context context, ProgressDialog alertDialog) {
        try {
            alertDialog.dismiss();
            alertDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeViewTreeObserver(View view, ViewTreeObserver.OnGlobalLayoutListener victim) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
            } else {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(victim);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideDialog(Context context, DialogInterface dialogInterface) {
        try {
            dialogInterface.dismiss();
            dialogInterface = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getTextLineCount(Context context, TextView textView, String textString, int widthTextView) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(textView.getTypeface());
        textPaint.setTextSize(textView.getTextSize());
        textPaint.setTextAlign(Paint.Align.LEFT);
        StaticLayout staticLayout = new StaticLayout(textString, textPaint, widthTextView, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true);

        return staticLayout.getLineCount();
    }

    public static AlertDialog makeSimpleAlertDialog(String title, String message, String positiveText, final Context context, DialogInterface.OnClickListener positiveClickListener) {
        if (positiveText == null) {
            positiveText = "OK";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        if (title != null) {
            builder.setTitle(title);
        }

        if (positiveClickListener == null) {
            builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (context != null) {
                        UIUtils.hideDialog(context, dialogInterface);
                    }
                }
            });
        } else {
            builder.setPositiveButton(positiveText, positiveClickListener);
        }

        AlertDialog alertDialog = builder.create();
        alertDialog = showDialog(context, alertDialog);
        return alertDialog;
    }

    public static AlertDialog makeSimpleAlertDialog(String title, String message, String positiveText, final Context context) {
        return makeSimpleAlertDialog(title, message, positiveText, context, null);
    }

    public static AlertDialog makeSimpleAlertDialog(String title, String message, final Context context) {
        return makeSimpleAlertDialog(title, message, null, context, null);
    }
}
