package com.example.foodplanner.features.common.helpers;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.foodplanner.features.common.helpers.convertors.LocalDateConvertor;
import com.example.foodplanner.features.common.models.MealItem;

import java.time.LocalDate;
import java.util.TimeZone;

public class MealCalenderHelper {
    private static final String TAG = "MealCalenderHelper";
    private final ContentResolver contentResolver;
    private final CalendarPermissionHolder calendarPermissionHolder;
    private Long calenderId;

    public MealCalenderHelper(ContentResolver contentResolver,
                              CalendarPermissionHolder calendarPermissionHolder) {
        Log.d(TAG, "MealCalenderHelper: " + calendarPermissionHolder);
        this.contentResolver = contentResolver;
        this.calendarPermissionHolder = calendarPermissionHolder;
    }

    public String addMeal(LocalDate date, MealItem meal) {
        if (calendarPermissionHolder.hasPermissions()) {
            Long calenderId = this.calenderId;
            if (calenderId == null) {
                calenderId = getCalenderId();
                synchronized (this) {
                    this.calenderId = calenderId;
                }
            }
            if (calenderId == null) return null;

            long startMillis = LocalDateConvertor.fromDate(date);
            long endMillis = LocalDateConvertor.fromDate(date.plusDays(1));

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, meal.getName());
            values.put(CalendarContract.Events.DESCRIPTION, "Cook " + meal.getName());
            values.put(CalendarContract.Events.CALENDAR_ID, calenderId);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri != null) {
                Log.d(TAG, "addMeal: " + uri);
                return uri.toString();
            }
        }
        return null;
    }

    public void removeMeal(String uri) {
        if (calendarPermissionHolder.hasPermissions()) {
            Uri deleteUri = Uri.parse(uri);
            contentResolver.delete(deleteUri, null, null);
        }
    }

    private Long getCalenderId() {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = {CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME};
        try (Cursor cursor = contentResolver.query(uri, projection, null, null, null)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    @SuppressLint("Range")
                    Long calID = cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                    Log.d(TAG, "getCalenderId: " + calID);
                    return calID;
                }
            }
        }
        return null;
    }
}
