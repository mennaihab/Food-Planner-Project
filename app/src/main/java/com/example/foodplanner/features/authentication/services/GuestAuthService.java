package com.example.foodplanner.features.authentication.services;

import android.app.Activity;

import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.example.foodplanner.features.authentication.helpers.EmailLoginCredentials;
import com.example.foodplanner.features.authentication.helpers.EmailSignupCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class GuestAuthService implements LoginServiceContract<Void> {
    private static final String TAG = "EmailAuthService";
    private static GuestAuthService instance;
    private final AuthenticationHelper authenticationHelper;
    private final FirebaseAuth firebaseAuth;

    private GuestAuthService(FirebaseAuth firebaseAuth, AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
        this.firebaseAuth = firebaseAuth;
    }

    public synchronized static GuestAuthService create(FirebaseAuth firebaseAuth,
                                                       AuthenticationHelper firebaseAuthService) {
        if (instance == null) {
            instance = new GuestAuthService(firebaseAuth, firebaseAuthService);
        }
        return instance;
    }

    @Override
    public void login(Activity activity, Void ignored) {
        authenticationHelper.onAuthTask(
                AppAuthResult.Provider.GUEST,
                firebaseAuth.signInAnonymously()
        );
    }
}
