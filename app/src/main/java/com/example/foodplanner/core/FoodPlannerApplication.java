package com.example.foodplanner.core;

import android.app.Application;
import android.content.Context;

import com.example.foodplanner.features.common.helpers.OperationManager;
import com.example.foodplanner.features.common.helpers.OperationManagerImpl;
import com.facebook.appevents.AppEventsLogger;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FoodPlannerApplication extends Application {

    private final OperationManager operationManager = new OperationManagerImpl();

    public static FoodPlannerApplication from(Context context) {
        return (FoodPlannerApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onTerminate() {
        operationManager.close();
        super.onTerminate();
    }

    public OperationManager getOperationManager() {
        return operationManager;
    }
}
