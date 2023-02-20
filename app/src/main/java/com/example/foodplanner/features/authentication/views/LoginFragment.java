package com.example.foodplanner.features.authentication.views;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.TextSpan;
import com.example.foodplanner.core.helpers.FormFieldValidator;
import com.example.foodplanner.core.utils.FormUtils;
import com.example.foodplanner.core.utils.SpanUtils;
import com.example.foodplanner.core.utils.TextUtils;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.authentication.helpers.EmailLoginCredentials;
import com.example.foodplanner.features.authentication.presenters.AuthenticationPresenter;
import com.example.foodplanner.features.common.views.OperationSink;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment implements AuthenticationView {

    private static final String TAG = "LoginFragment";

    private OperationSink operationSink;
    private AuthenticationPresenter presenter;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    // TODO: use string resources

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        operationSink = (OperationSink) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = AuthenticationPresenter.create(requireContext(), getViewLifecycleOwner(), this, operationSink);

        setupForgotPassword(view.findViewById(R.id.forgot_password_tv));

        EditText email = view.findViewById(R.id.email_edit_text);
        EditText password = view.findViewById(R.id.password_edit_text);

        Button loginButton = view.findViewById(R.id.login_btn);

        EditText[] fields = new EditText[]{email, password};

        loginButton.setOnClickListener(e -> {
            String[] errors = FormUtils.validateForm(fields);
            if (errors.length > 0) {
                showError(errors[0]);
                return;
            }
            ViewUtils.hideKeyboard(e);
            presenter.loginUser(requireActivity(),
                    new EmailLoginCredentials(
                            email.getText().toString(),
                            password.getText().toString()
                    )
            );
        });

        FormUtils.attachValidator(email, emailText -> {
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

        FormUtils.attachValidator(password, passwordText -> {
            if (passwordText.length() <= 0) {
                return "enter your password";
            }
            return null;
        });

    }



    private void setupForgotPassword(TextView textView) {
        SpannableString spannableString = SpanUtils.createSpannable("I FORGOT MY PASSWORD!",
                TextSpan.of(SpanUtils.createClickableSpan(e -> {
                    Navigation.findNavController(e).navigate(LoginFragmentDirections.actionLoginToForgotPassword());
                }), 0)
        );
        textView.setText(spannableString);
        TextUtils.makeClickable(textView);
        textView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAuthResult(AppAuthResult authResult) {
        Log.d(TAG, "onAuthResult: " + authResult);
        if (authResult instanceof AppAuthResult.Success) {
            Navigation.findNavController(requireView()).navigate(AuthenticationFragmentDirections.actionGlobalMainGraph());
        } else if (authResult instanceof AppAuthResult.Failure) {
            Throwable error = ((AppAuthResult.Failure) authResult).getError();
            if (error != null) {
                String message = error.getLocalizedMessage();
                if (message == null) {
                    message = error.getMessage();
                }
                showError(message);
            }

        }
    }

    private void showError(String error) {
        Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show();
    }
}
