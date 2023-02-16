package com.example.foodplanner.features.authentication.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.example.foodplanner.features.authentication.services.FacebookAuthService;
import com.example.foodplanner.features.authentication.services.GoogleAuthService;
import com.example.foodplanner.features.authentication.services.GuestAuthService;
import com.example.foodplanner.features.authentication.services.LoginServiceContract;
import com.example.foodplanner.features.authentication.views.AuthenticationView;
import com.example.foodplanner.features.common.helpers.Operation;
import com.example.foodplanner.features.common.views.OperationSink;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.CompletableSubject;

public class AuthenticationPresenter {

    private final AuthenticationView authenticationView;
    private final OperationSink operationSink;
    private final AuthenticationHelper authenticationHelper;
    private final FacebookAuthService facebookAuthService;
    private final GoogleAuthService googleAuthService;
    private final GuestAuthService guestAuthService;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public AuthenticationPresenter(AuthenticationView authenticationView,
                                   AuthenticationHelper authenticationHelper,
                                   OperationSink operationSink,
                                   FacebookAuthService facebookAuthService,
                                   GoogleAuthService googleAuthService,
                                   GuestAuthService guestAuthService) {
        this.authenticationView = authenticationView;
        this.authenticationHelper = authenticationHelper;
        this.operationSink = operationSink;
        this.facebookAuthService = facebookAuthService;
        this.googleAuthService = googleAuthService;
        this.guestAuthService = guestAuthService;
        disposable.add(authenticationHelper.getAuthResult()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authenticationView::onAuthResult));
    }

    public static AuthenticationPresenter create(Context context,
                                                 AuthenticationView view,
                                                 OperationSink operationSink) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        AuthenticationHelper authenticationHelper = AuthenticationHelper.create(firebaseAuth);

        return new AuthenticationPresenter(view,
                authenticationHelper,
                operationSink,
                FacebookAuthService.create(authenticationHelper),
                GoogleAuthService.create(context, authenticationHelper),
                GuestAuthService.create(firebaseAuth, authenticationHelper)
        );
    }

    public void onActivityResult(Activity activity, int requestCode, @Nullable Intent data) {
        googleAuthService.onActivityResult(activity, requestCode, data);
    }

    public void loginFacebook(Activity activity) {
        login(activity, facebookAuthService);
    }

    public void loginGoogle(Activity activity) {
        login(activity, googleAuthService);
    }

    public void loginGuest(Activity activity) {
        login(activity, guestAuthService);
    }

    private void login(Activity activity, LoginServiceContract<?> loginService) {
        operationSink.submitOperation(Operation.fromSingle(authenticationHelper.getAuthResult().firstOrError()));
        loginService.login(activity, null);
    }

    private void close() {
        disposable.dispose();
    }
}
