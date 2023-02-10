package com.example.foodplanner.features.authentication.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.TextSpan;
import com.example.foodplanner.core.utils.SpanUtils;
import com.example.foodplanner.core.utils.TextUtils;
import com.google.android.material.internal.TextWatcherAdapter;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    // TODO: use string resources

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupForgotPassword(view.findViewById(R.id.forgot_password_tv));
        EditText email = view.findViewById(R.id.email_edit_text);

        attachValidator(email, emailText -> {
            if (emailText.length() <= 0) {
                return "enter your email";
            } else {
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex); // TODO: Scope to activity
                Matcher matcher = pattern.matcher(emailText);
                if (!matcher.matches()) {
                    return "not a valid mail";
                }
            }
            return null;
        });

    }

    private void setupForgotPassword(TextView textView) {
        SpannableString spannableString = SpanUtils.createSpannable("I FORGOT MY PASSWORD!",
                TextSpan.of(SpanUtils.createClickableSpan(e -> {
                    Navigation.findNavController(e).navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment());
                }), 0)
        );
        textView.setText(spannableString);
        TextUtils.makeClickable(textView);
    }



    @SuppressLint("RestrictedApi")
    private void attachValidator(EditText editText, Function<String, String> validator) {
        editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(@NonNull Editable s) {
                String text = s.toString();
                editText.setError(validator.apply(text));
            }
        });
    }
}
