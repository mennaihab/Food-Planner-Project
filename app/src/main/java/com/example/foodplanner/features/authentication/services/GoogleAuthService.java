package com.example.foodplanner.features.authentication.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.foodplanner.R;
import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleAuthService implements LoginServiceContract<Void>, SignupServiceContract<Void> {
    private static final String TAG = "GoogleLoginService";
    private static final int REQ_ONE_TAP = 10001;
    private static GoogleAuthService instance;

    private final BeginSignInRequest signInRequest;
    private final AuthenticationHelper authenticationHelper;
    private SignInClient oneTapClient;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private GoogleAuthService(Context context, AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(context.getString(R.string.google_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .setAutoSelectEnabled(true)
                .build();
    }

    public synchronized static GoogleAuthService create(Context context, AuthenticationHelper firebaseAuth) {
        if (instance == null) {
            instance = new GoogleAuthService(context, firebaseAuth);
        }
        return instance;
    }

    public void init(ComponentActivity activity) {
        Log.d(TAG, "init");
        launcher = activity.registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            onActivityResult(activity, result.getData(), REQ_ONE_TAP);
        });
    }

    public void onActivityResult(Activity activity, Intent intent, int requestCode) {
        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = ensureHasClient(activity).getSignInCredentialFromIntent(intent);
                String idToken = credential.getGoogleIdToken();
                if (idToken !=  null) {
                    Log.d(TAG, "handleSignInResult:success");
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    authenticationHelper.onAuthSuccess(AppAuthResult.Provider.GOOGLE, firebaseCredential);
                } else {
                    throw new ApiException(Status.RESULT_DEAD_CLIENT);
                }
            } catch (ApiException e) {
                Log.w(TAG, "handleSignInResult:failure", e);
                authenticationHelper.onFailure(AppAuthResult.Provider.GOOGLE, e);
            }
        }
    }

    @Override
    public void signup(Activity activity, Void ignored) {
        authenticate(activity);
    }

    @Override
    public void login(Activity activity, Void ignored) {
        authenticate(activity);
    }

    public void authenticate(Activity activity) {
        ensureHasClient(activity).beginSignIn(signInRequest).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCredential:success");
                if (launcher != null) {
                    IntentSenderRequest request = new IntentSenderRequest.Builder(task.getResult().getPendingIntent()).build();
                    launcher.launch(request);
                } else {
                    try {
                        activity.startIntentSenderForResult(
                                task.getResult().getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Log.w(TAG, "signInWithCredential:failure", e);
                        authenticationHelper.onFailure(AppAuthResult.Provider.GOOGLE, e);
                    }
                }
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                authenticationHelper.onFailure(AppAuthResult.Provider.GOOGLE, task.getException());
            }
        });
    }

    private synchronized SignInClient ensureHasClient(Activity activity) {
        if (oneTapClient == null) {
            oneTapClient = Identity.getSignInClient(activity);
        }
        return oneTapClient;
    }
}
