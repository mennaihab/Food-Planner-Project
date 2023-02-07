package com.example.foodplanner.features.authentication.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
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
import com.example.foodplanner.features.common.views.WindowPainter;

public class AuthenticationFragment extends Fragment {

    private WindowPainter windowPainter;

    public AuthenticationFragment() {
        super(R.layout.fragment_authentication);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        windowPainter = (WindowPainter) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        windowPainter.setStatusBarVisibility(false);

        setupLogin(view.findViewById(R.id.login_tv));
        setupDisclaimer(view.findViewById(R.id.disclaimer_tv));

        view.<Button>findViewById(R.id.skip_btn).setOnClickListener(e->{
            Navigation.findNavController(e).navigate(AuthenticationFragmentDirections.actionAuthenticationToHomeMeal());
        });

        view.<Button>findViewById(R.id.signup_btn).setOnClickListener(e->{
            Navigation.findNavController(e).navigate(AuthenticationFragmentDirections.actionAuthenticationToSignup());
        });
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
}