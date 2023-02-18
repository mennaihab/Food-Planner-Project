package com.example.foodplanner.features.authentication.helpers;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class AuthenticationHelper {
    private static AuthenticationHelper instance;
    private static final String TAG = "FirebaseLoginService";
    private final FirebaseAuth firebaseAuth;

    private final PublishSubject<AppAuthResult> authResult;

    private AuthenticationHelper(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.authResult = PublishSubject.create();
    }

    public synchronized static AuthenticationHelper create(FirebaseAuth firebaseAuth) {
        if (instance == null) {
            instance = new AuthenticationHelper(firebaseAuth);
        }
        return instance;
    }

    public Observable<AppAuthResult> getAuthResult() {
        return authResult.subscribeOn(Schedulers.io());
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
                authResult.onNext(new AppAuthResult.Success(provider, result.getResult().getUser()));
            } else {
                Log.w(TAG, "signInWithCredential:failure", result.getException());
                onFailure(provider, result.getException());
            }
        });
    }
}
