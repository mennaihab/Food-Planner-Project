package com.example.foodplanner.features.authentication.services;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.DeviceLoginManager;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import java.util.Arrays;

public class FacebookAuthService implements LoginServiceContract<Void>, SignupServiceContract<Void> {
    private static final String TAG = "FacebookLoginService";
    private static final String[] permissions = new String[]{"public_profile", "email"};
    private static FacebookAuthService instance;
    private final LoginManager loginManager;

    private FacebookAuthService(AuthenticationHelper authenticationHelper,
                                CallbackManager callbackManager,
                                LoginManager loginManager) {
        this.loginManager = loginManager;
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess: " + loginResult);
                String token = loginResult.getAccessToken().getToken();
                AuthCredential credential = FacebookAuthProvider.getCredential(token);
                authenticationHelper.onAuthSuccess(AppAuthResult.Provider.FACEBOOK, credential);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                authenticationHelper.onCancellation(AppAuthResult.Provider.FACEBOOK);
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                authenticationHelper.onFailure(AppAuthResult.Provider.FACEBOOK, error);
            }
        });
    }

    public synchronized static FacebookAuthService create(AuthenticationHelper authenticationHelper) {
        if (instance == null) {
            instance = new FacebookAuthService(
                    authenticationHelper,
                    CallbackManager.Factory.create(),
                    DeviceLoginManager.getInstance()
            );
        }
        return instance;
    }

    @Override
    public void login(Activity activity, Void ignored) {
        authenticate(activity);
    }

    @Override
    public void signup(Activity activity, Void ignored) {
        authenticate(activity);
    }

    public void authenticate(Activity activity) {
        loginManager.logIn(activity, Arrays.asList(permissions));
    }
}
