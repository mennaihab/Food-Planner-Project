package com.example.foodplanner.features.common.helpers;

import android.util.Log;

import com.example.foodplanner.core.utils.UserUtils;
import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.common.services.AuthenticationManager;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class AuthenticationManagerImpl implements AuthenticationManager {
    private static final String TAG = "AuthenticationManagerImpl";

    private final FirebaseAuth firebaseAuth;
    private final BehaviorSubject<Optional<FirebaseUser>> currentUser;

    public AuthenticationManagerImpl(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        currentUser = BehaviorSubject.create();
        firebaseAuth.addAuthStateListener(auth -> {
            Log.d(TAG, "AuthStateListener: " + auth.getCurrentUser());
            currentUser.onNext(Optional.ofNullable(auth.getCurrentUser()));
        });
    }

    @Override
    public boolean isAuthenticated() {
        return UserUtils.isPresent(Optional.ofNullable(firebaseAuth.getCurrentUser()));
    }

    @Override
    public boolean isLoading() {
        return !currentUser.hasValue();
    }

    @Override
    public Optional<FirebaseUser> getCurrentUser() {
        Optional<FirebaseUser> user = currentUser.getValue();
        if (user != null) return user;
        return Optional.empty();
    }

    @Override
    public Observable<Optional<FirebaseUser>> getCurrentUserObservable() {
        return currentUser;
    }

    @Override
    public AppAuthResult.Provider getCurrentProvider() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isAnonymous()) {
                return AppAuthResult.Provider.GUEST;
            }
            for (UserInfo info: currentUser.getProviderData()) {
                switch (info.getProviderId()) {
                    case GoogleAuthProvider.PROVIDER_ID:
                        return AppAuthResult.Provider.GOOGLE;
                    case FacebookAuthProvider.PROVIDER_ID:
                        return AppAuthResult.Provider.FACEBOOK;
                    case EmailAuthProvider.PROVIDER_ID:
                        return AppAuthResult.Provider.EMAIL;
                }
            }
        }
        return null;
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }
}
