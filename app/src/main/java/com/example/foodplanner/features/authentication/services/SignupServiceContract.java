package com.example.foodplanner.features.authentication.services;

import android.app.Activity;
import android.content.Context;

import androidx.activity.ComponentActivity;

import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.google.firebase.auth.FirebaseAuth;

public interface SignupServiceContract<T> {
    void signup(ComponentActivity activity, T credentials);

    static SignupServiceContract<?> create(AppAuthResult.Provider provider,
                                           Context context,
                                           FirebaseAuth firebaseAuth,
                                           AuthenticationHelper authenticationHelper) {
        switch (provider) {
            case FACEBOOK:
                return FacebookAuthService.create(authenticationHelper);
            case GOOGLE:
                return GoogleAuthService.create(context, authenticationHelper);
            case EMAIL:
                return EmailAuthService.create(firebaseAuth, authenticationHelper);
            case GUEST:
                throw new RuntimeException("Unsupported provider " + provider);
            default:
                throw new RuntimeException("Unknown provider " + provider);
        }
    }
}
