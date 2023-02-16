package com.example.foodplanner.core;

import android.app.Application;
import android.content.Context;

import com.facebook.appevents.AppEventsLogger;

public class FoodPlannerApplication extends Application {

    public static FoodPlannerApplication from(Context context) {
        return (FoodPlannerApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);
    }
}
