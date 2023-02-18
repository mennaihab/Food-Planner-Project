package com.example.foodplanner.features.authentication.services;

import android.app.Activity;

import androidx.activity.ComponentActivity;

import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.example.foodplanner.features.authentication.helpers.EmailLoginCredentials;
import com.example.foodplanner.features.authentication.helpers.EmailSignupCredentials;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class EmailAuthService implements LoginServiceContract<EmailLoginCredentials>, SignupServiceContract<EmailSignupCredentials> {
    private static final String TAG = "EmailAuthService";
    private static EmailAuthService instance;
    private final AuthenticationHelper authenticationHelper;
    private final FirebaseAuth firebaseAuth;

    private EmailAuthService(FirebaseAuth firebaseAuth, AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
        this.firebaseAuth = firebaseAuth;
    }

    public synchronized static EmailAuthService create(FirebaseAuth firebaseAuth,
                                                       AuthenticationHelper firebaseAuthService) {
        if (instance == null) {
            instance = new EmailAuthService(firebaseAuth, firebaseAuthService);
        }
        return instance;
    }

    @Override
    public void signup(ComponentActivity activity, EmailSignupCredentials credentials) {
        authenticationHelper.onAuthTask(
                AppAuthResult.Provider.EMAIL,
                firebaseAuth.createUserWithEmailAndPassword(
                        credentials.getEmail(),
                        credentials.getPassword()
                ).addOnSuccessListener(authResult -> {
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(credentials.getUsername()).build();
                    Objects.requireNonNull(authResult.getUser()).updateProfile(request);
                })
        );
    }

    @Override
    public void login(ComponentActivity activity, EmailLoginCredentials credentials) {
        authenticationHelper.onAuthSuccess(
                AppAuthResult.Provider.EMAIL,
                EmailAuthProvider.getCredential(
                        credentials.getEmail(),
                        credentials.getPassword()
                )
        );
    }
}
