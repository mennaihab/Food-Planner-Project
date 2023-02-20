package com.example.foodplanner.features.authentication.presenters;

import android.content.Context;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.core.helpers.LifecycleObservable;
import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.example.foodplanner.features.authentication.helpers.EmailLoginCredentials;
import com.example.foodplanner.features.authentication.helpers.EmailSignupCredentials;
import com.example.foodplanner.features.authentication.services.EmailAuthService;
import com.example.foodplanner.features.authentication.services.FacebookAuthService;
import com.example.foodplanner.features.authentication.services.GoogleAuthService;
import com.example.foodplanner.features.authentication.services.GuestAuthService;
import com.example.foodplanner.features.authentication.services.LoginServiceContract;
import com.example.foodplanner.features.authentication.services.SignupServiceContract;
import com.example.foodplanner.features.authentication.views.AuthenticationView;
import com.example.foodplanner.features.common.views.OperationSink;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AuthenticationPresenter {

    private final OperationSink operationSink;
    private final AuthenticationHelper authenticationHelper;
    private final FacebookAuthService facebookAuthService;
    private final GoogleAuthService googleAuthService;
    private final GuestAuthService guestAuthService;
    private final EmailAuthService emailAuthService;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public AuthenticationPresenter(LifecycleOwner lifecycleOwner,
                                   AuthenticationView authenticationView,
                                   AuthenticationHelper authenticationHelper,
                                   OperationSink operationSink,
                                   FacebookAuthService facebookAuthService,
                                   GoogleAuthService googleAuthService,
                                   GuestAuthService guestAuthService,
                                   EmailAuthService emailAuthService) {
        this.authenticationHelper = authenticationHelper;
        this.operationSink = operationSink;
        this.facebookAuthService = facebookAuthService;
        this.googleAuthService = googleAuthService;
        this.guestAuthService = guestAuthService;
        this.emailAuthService = emailAuthService;
        Observable<Lifecycle.State> lifecycle = LifecycleObservable.fromLifecycleOwner(lifecycleOwner);
        disposable.add(authenticationHelper.getAuthResult()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(authResult -> lifecycle.filter(state -> state.isAtLeast(Lifecycle.State.RESUMED)).map(ignored -> authResult))
                .subscribe(authenticationView::onAuthResult));
    }

    public static AuthenticationPresenter create(Context context,
                                                 LifecycleOwner lifecycleOwner,
                                                 AuthenticationView view,
                                                 OperationSink operationSink) {
        FirebaseAuth firebaseAuth = FoodPlannerApplication.from(context).getFirebaseAuth();
        AuthenticationHelper authenticationHelper = AuthenticationHelper.create(firebaseAuth);

        return new AuthenticationPresenter(lifecycleOwner, view,
                authenticationHelper,
                operationSink,
                FacebookAuthService.create(authenticationHelper),
                GoogleAuthService.create(context, authenticationHelper),
                GuestAuthService.create(firebaseAuth, authenticationHelper),
                EmailAuthService.create(firebaseAuth, authenticationHelper)
        );
    }

    public void loginFacebook(ComponentActivity activity) {
        login(activity, facebookAuthService);
    }

    public void loginGoogle(ComponentActivity activity) {
        login(activity, googleAuthService);
    }

    public void loginGuest(ComponentActivity activity) {
        login(activity, guestAuthService);
    }

    public void loginUser(ComponentActivity activity, EmailLoginCredentials credentials) {
        login(activity, emailAuthService, credentials);
    }

    public void signupUser(ComponentActivity activity, EmailSignupCredentials credentials) {
        signup(activity, emailAuthService, credentials);
    }

    private void login(ComponentActivity activity, LoginServiceContract<?> loginService) {
        login(activity, loginService, null);
    }

    private <T> void signup(ComponentActivity activity, SignupServiceContract<T> signupServiceContract, T credentials) {
        operationSink.submitOperation(authenticationHelper.getAuthResult().firstElement().ignoreElement());
        signupServiceContract.signup(activity, credentials);
    }

    private <T> void login(ComponentActivity activity, LoginServiceContract<T> loginService, T credentials) {
        operationSink.submitOperation(authenticationHelper.getAuthResult().firstElement().ignoreElement());
        loginService.login(activity, credentials);
    }

    private void close() {
        disposable.dispose();
    }
}
