package com.ashishgoel.got.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ashish on 09/10/16.
 */

public class ImageUtils {

    public static void loadProfilePicFromUrl(String name, ImageView imageView, Context context, int radius) {
        try {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(name);

            String firstLetter = "";

            if (!AndroidUtils.isEmpty(name)) {
                firstLetter = Character.toString(name.charAt(0)).toUpperCase();
            }

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(radius)  // width in px
                    .height(radius) // height in px
                    .endConfig()
                    .buildRound(firstLetter, color);

            imageView.setImageDrawable(drawable);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (width <= maxSize && height <= maxSize) {
            return image;
        } else {
            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        }
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // reduce from 100 to 80 or lower for less image size
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return encoded;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
