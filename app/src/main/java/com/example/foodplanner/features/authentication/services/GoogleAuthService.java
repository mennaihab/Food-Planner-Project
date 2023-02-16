package com.example.foodplanner.features.authentication.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

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

    }

    public void onActivityResult(Activity activity, int requestCode, Intent intent) {
        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = ensureHasClient(activity).getSignInCredentialFromIntent(intent);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
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
    public void signup(ComponentActivity activity, Void ignored) {
        authenticate(activity);
    }

    @Override
    public void login(ComponentActivity activity, Void ignored) {
        authenticate(activity);
    }

    private void authenticate(ComponentActivity activity) {
        ensureHasClient(activity).beginSignIn(signInRequest).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCredential:success");
                launcher = activity.getActivityResultRegistry().register("google-login", new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                    try {
                        onActivityResult(activity, REQ_ONE_TAP, result.getData());
                    } finally {
                        launcher.unregister();
                        launcher = null;
                    }
                });
                IntentSenderRequest request = new IntentSenderRequest.Builder(task.getResult().getPendingIntent()).build();
                launcher.launch(request);
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
