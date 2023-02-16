package com.example.foodplanner.features.authentication.helpers;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class AuthenticationHelper {
    private static AuthenticationHelper instance;
    private static final String TAG = "FirebaseLoginService";
    private final FirebaseAuth firebaseAuth;

    private final BehaviorSubject<AppAuthResult> authResult;

    private AuthenticationHelper(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.authResult = BehaviorSubject.create();
    }

    public synchronized static AuthenticationHelper create(FirebaseAuth firebaseAuth) {
        if (instance == null) {
            instance = new AuthenticationHelper(firebaseAuth);
        }
        return instance;
    }

    public Observable<AppAuthResult> getAuthResult() {
        return authResult;
    }

    public void onFailure(AppAuthResult.Provider provider, Throwable error) {
        authResult.onNext(new AppAuthResult.Failure(provider, error));
    }

    public void onCancellation(AppAuthResult.Provider provider) {
        authResult.onNext(new AppAuthResult.Cancellation(provider));
    }

    public void onAuthSuccess(AppAuthResult.Provider provider, AuthCredential credential) {
        Log.d(TAG, "handleCredentials: " + credential);
        onAuthTask(provider, firebaseAuth.signInWithCredential(credential));
    }

    public void onAuthTask(AppAuthResult.Provider provider, Task<AuthResult> task) {
        task.addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                Log.d(TAG, "signInWithCredential:success");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                authResult.onNext(new AppAuthResult.Success(provider, user));
            } else {
                Log.w(TAG, "signInWithCredential:failure", result.getException());
                onFailure(provider, result.getException());
            }
        });
    }

}