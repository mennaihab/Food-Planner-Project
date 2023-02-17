package com.example.foodplanner.core;

import android.app.Application;
import android.content.Context;

import com.example.foodplanner.features.common.helpers.AuthenticationManagerImpl;
import com.example.foodplanner.features.common.services.AuthenticationManager;
import com.example.foodplanner.features.common.services.OperationManager;
import com.example.foodplanner.features.common.helpers.OperationManagerImpl;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class FoodPlannerApplication extends Application {

    private final OperationManager operationManager = new OperationManagerImpl();
    private AuthenticationManager authenticationManager;

    public static FoodPlannerApplication from(Context context) {
        return (FoodPlannerApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        AppEventsLogger.activateApp(this);
        authenticationManager = new AuthenticationManagerImpl(FirebaseAuth.getInstance());
    }

    @Override
    public void onTerminate() {
        operationManager.close();
        super.onTerminate();
    }

    public OperationManager getOperationManager() {
        return operationManager;
    }
    public AuthenticationManager getAuthenticationManager() { return authenticationManager; }
}
