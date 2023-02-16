package com.example.foodplanner.features.authentication.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.TextSpan;
import com.example.foodplanner.core.utils.SpanUtils;
import com.example.foodplanner.core.utils.TextUtils;
import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.example.foodplanner.features.authentication.presenters.AuthenticationPresenter;
import com.example.foodplanner.features.authentication.services.FacebookAuthService;
import com.example.foodplanner.features.authentication.services.GoogleAuthService;
import com.example.foodplanner.features.authentication.services.GuestAuthService;
import com.example.foodplanner.features.common.views.OperationSink;
import com.example.foodplanner.features.common.views.WindowPainter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Completable;

public class AuthenticationFragment extends Fragment implements AuthenticationView {
    private static final String TAG = "AuthenticationFragment";

    private WindowPainter windowPainter;
    private OperationSink operationSink;
    private AuthenticationPresenter presenter;

    public AuthenticationFragment() {
        super(R.layout.fragment_authentication);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        windowPainter = (WindowPainter) context;
        operationSink = (OperationSink) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        windowPainter.setStatusBarVisibility(false);

        presenter = AuthenticationPresenter.create(requireContext(), this, operationSink);

        setupLogin(view.findViewById(R.id.login_tv));
        setupDisclaimer(view.findViewById(R.id.disclaimer_tv));

        view.findViewById(R.id.facebook_btn).setOnClickListener(e -> {
            presenter.loginFacebook(requireActivity());
        });

        view.findViewById(R.id.google_btn).setOnClickListener(e -> {
            presenter.loginGoogle(requireActivity());
        });

        view.<Button>findViewById(R.id.skip_btn).setOnClickListener(e->{
            presenter.loginGuest(requireActivity());
        });

        view.<Button>findViewById(R.id.signup_btn).setOnClickListener(e->{
            Navigation.findNavController(e).navigate(AuthenticationFragmentDirections.actionAuthenticationToSignup());
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.onActivityResult(requireActivity(), requestCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupLogin(TextView textView) {
        SpannableString spannableString = SpanUtils.createSpannable("Already have an account? Login",
                TextSpan.of(SpanUtils.createClickableSpan(e -> {
                    Navigation.findNavController(e).navigate(AuthenticationFragmentDirections.actionAuthenticationToLogin());
                }), 25)
        );
        textView.setText(spannableString);
        TextUtils.makeClickable(textView);
    }

    private void setupDisclaimer(TextView textView) {
        SpannableString spannableString = SpanUtils.createSpannable("By using FOODA, you agree to our Privacy Notice and Terms of Use",
                TextSpan.of(SpanUtils.createClickableSpan(e -> {}), 33, 47, Spanned.SPAN_INCLUSIVE_INCLUSIVE),
                TextSpan.of(SpanUtils.createClickableSpan(e -> {}), 52)
        );
        textView.setText(spannableString);
        TextUtils.makeClickable(textView);
    }

    @Override
    public void onAuthResult(AppAuthResult authResult) {
        Log.d(TAG, "onAuthResult: " + authResult);
        if (authResult instanceof AppAuthResult.Success) {
            Navigation.findNavController(requireView())
                    .navigate(AuthenticationFragmentDirections.actionAuthenticationToHomeMeal());
        } else if (authResult instanceof AppAuthResult.Failure) {
            Throwable error = ((AppAuthResult.Failure) authResult).getError();
            if (error != null) {
                String message = error.getLocalizedMessage();
                if (message == null) {
                    message = error.getMessage();
                }
                Snackbar.make(requireView(), Objects.requireNonNull(message), Snackbar.LENGTH_LONG).show();
            }

        }
    }
}