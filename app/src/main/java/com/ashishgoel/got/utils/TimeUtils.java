package com.ashishgoel.got.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    public static final String PARSER_FORMAT_FOR_DATES = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String getCurrentTimeString() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(PARSER_FORMAT_FOR_DATES);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }

    public static CharSequence getPostTime(String timestamp) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    PARSER_FORMAT_FOR_DATES);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = simpleDateFormat.parse(timestamp);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());

            CharSequence simpleDate = DateUtils.getRelativeTimeSpanString(
                    date.getTime(), Calendar.getInstance().getTimeInMillis(),
                    DateUtils.MINUTE_IN_MILLIS);

            if (simpleDate.equals("0 minutes ago"))
                simpleDate = "Just Now";

            return simpleDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Just Now";
    }

    public static String getSimpleDate(String timestamp) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    PARSER_FORMAT_FOR_DATES, Locale.US);
            Date date = simpleDateFormat.parse(timestamp);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            String simpleDate = Integer.toString(calendar
                    .get(Calendar.DAY_OF_MONTH))
                    + Integer.toString(calendar.get(Calendar.MONTH))
                    + Integer.toString(calendar.get(Calendar.YEAR)) + "";
            return simpleDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getChatTime(String timestamp) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    PARSER_FORMAT_FOR_DATES, Locale.US);
            Date date = simpleDateFormat.parse(timestamp);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());

            String am_or_pm = calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM"
                    : " PM";

            String minutes = Integer.toString(calendar.get(Calendar.MINUTE));

            if (calendar.get(Calendar.MINUTE) < 10) {
                minutes = "0" + Integer.toString(calendar.get(Calendar.MINUTE));
            }

            String hour = Integer.toString(calendar.get(Calendar.HOUR));
            if (hour.equals("0"))
                hour = "12";

            String simpleDate = hour + ":" + minutes + am_or_pm;
            return simpleDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Just Now";
    }

    public static String getChatDateDisplayed(String timestamp) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    PARSER_FORMAT_FOR_DATES, Locale.US);
            Date dateObj = simpleDateFormat.parse(timestamp);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateObj.getTime());

            SpannableStringBuilder builder1 = new SpannableStringBuilder();

            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String dayName = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            builder1.append(dayName + ", ");

            String date = getMonth(calendar.get(Calendar.MONTH)) + " "
                    + String.format("%02d", day);

            SpannableString dateSpannable = new SpannableString(date);
            dateSpannable.setSpan(
                    new StyleSpan(android.graphics.Typeface.BOLD), 0,
                    date.length(), 0);
            builder1.append(dateSpannable);

            String year = " " + calendar.get(Calendar.YEAR);
            SpannableString yearSpannable = new SpannableString(year);
            yearSpannable.setSpan(
                    new StyleSpan(android.graphics.Typeface.BOLD), 0,
                    year.length(), 0);
            yearSpannable.setSpan(new RelativeSizeSpan(1.5f), 0, year.length(),
                    0);
            builder1.append(yearSpannable);

            return builder1.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDayOfWeek(int dayOfWeek) {
        String result = "";
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                result = "MONDAY";
                break;
            case Calendar.TUESDAY:
                result = "TUESDAY";
                break;
            case Calendar.WEDNESDAY:
                result = "WEDNESDAY";
                break;
            case Calendar.THURSDAY:
                result = "THURSDAY";
                break;
            case Calendar.FRIDAY:
                result = "FRIDAY";
                break;
            case Calendar.SATURDAY:
                result = "SATURDAY";
                break;
            case Calendar.SUNDAY:
                result = "SUNDAY";
                break;
        }
        return result;
    }

    public static String getMonth(int month) {

        String result = "";
        switch (month) {

            case Calendar.JANUARY:
                result = "JANUARY";
                break;

            case Calendar.FEBRUARY:
                result = "FEBRUARY";
                break;

            case Calendar.MARCH:
                result = "MARCH";
                break;

            case Calendar.APRIL:
                result = "APRIL";
                break;

            case Calendar.MAY:
                result = "MAY";
                break;

            case Calendar.JUNE:
                result = "JUNE";
                break;

            case Calendar.JULY:
                result = "JULY";
                break;

            case Calendar.AUGUST:
                result = "AUGUST";
                break;

            case Calendar.SEPTEMBER:
                result = "SEPTEMBER";
                break;

            case Calendar.OCTOBER:
                result = "OCTOBER";
                break;

            case Calendar.NOVEMBER:
                result = "NOVEMBER";
                break;

            case Calendar.DECEMBER:
                result = "DECEMBER";
                break;
        }
        return result;
    }

    public static CharSequence getNormalisedDateFromDatePicker(int year,
                                                               int monthOfYear, int dayOfMonth) {
        return dayOfMonth + " " + getMonth(monthOfYear) + " " + year;
    }

    public static CharSequence getNormalisedTimeFromTimePicker(int hourOfDay,
                                                               int minute) {
        String am_or_pm = hourOfDay >= 12 ? " PM" : " AM";

        String minutes = Integer.toString(minute);
        if (minute < 10) {
            minutes = "0" + Integer.toString(minute);
        }

        if (hourOfDay == 0)
            hourOfDay = 12;

        if (hourOfDay > 12) {
            hourOfDay = hourOfDay - 12;
        }

        String simpleDate = hourOfDay + ":" + minutes + am_or_pm;
        return simpleDate;
    }
}
