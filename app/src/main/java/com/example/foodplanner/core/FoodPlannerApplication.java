package com.example.foodplanner.core;

import android.app.Application;
import android.content.Context;

import com.example.foodplanner.features.common.helpers.AuthenticationManagerImpl;
import com.example.foodplanner.features.common.helpers.UserDataManagerImpl;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.repositories.SettingsRepository;
import com.example.foodplanner.features.common.services.AuthenticationManager;
import com.example.foodplanner.features.common.services.OperationManager;
import com.example.foodplanner.features.common.helpers.OperationManagerImpl;
import com.example.foodplanner.features.common.services.SettingsManager;
import com.example.foodplanner.features.common.services.UserDataManager;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FoodPlannerApplication extends Application {

    private final OperationManager operationManager = new OperationManagerImpl();
    private AuthenticationManager authenticationManager;
    private UserDataManager userDataManager;
    private SettingsManager settingsManager;

    public static FoodPlannerApplication from(Context context) {
        return (FoodPlannerApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        AppEventsLogger.activateApp(this);
        authenticationManager = new AuthenticationManagerImpl(getFirebaseAuth());
        settingsManager = SettingsRepository.create(this);
        userDataManager = UserDataManagerImpl.create(this, getFirestore(), authenticationManager);
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

    public SettingsManager getSettingsManager() { return settingsManager; }

    public UserDataManager getUserDataManager() { return userDataManager; }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }


}
