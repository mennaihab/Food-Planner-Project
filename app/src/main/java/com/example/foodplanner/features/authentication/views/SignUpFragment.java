package com.example.foodplanner.features.authentication.views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.FormFieldValidator;
import com.example.foodplanner.core.utils.FormUtils;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.example.foodplanner.features.authentication.helpers.EmailSignupCredentials;
import com.example.foodplanner.features.authentication.presenters.AuthenticationPresenter;
import com.example.foodplanner.features.common.views.OperationSink;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment implements AuthenticationView {
    private static final String TAG = "SignUpFragment";

    private OperationSink operationSink;
    private AuthenticationPresenter presenter;

    public SignUpFragment() {
        super(R.layout.fragment_signup);
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

        EditText name = view.findViewById(R.id.name_edit_text);
        EditText email = view.findViewById(R.id.email_edit_text);
        EditText password = view.findViewById(R.id.password_edit_text);
        EditText confirmPassword = view.findViewById(R.id.confirm_password_edit_text);
        Button signupButton = view.findViewById(R.id.signup_btn);

        EditText[] fields = new EditText[]{name, email, password, confirmPassword};

        signupButton.setOnClickListener(e -> {
            String[] errors = FormUtils.validateForm(fields);
            if (errors.length > 0) {
                showError(errors[0]);
                return;
            }
            ViewUtils.hideKeyboard(e);
            presenter.signupUser(requireActivity(),
                    new EmailSignupCredentials(
                            name.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString()
                    )
            );
        });

        FormUtils.attachValidator(name, nameText -> {
            if (nameText.length() <= 0) {
                return "enter your name";
            }
            return null;
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
            String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
            Pattern pattern = Pattern.compile(regex); // TODO: Scope to activity
            Matcher matcher = pattern.matcher(passwordText);
            if (passwordText.length() <= 0) {
                return "enter your password";
            } else if (!matcher.matches()) {
                return "password length should be at least 8 char\n" +
                        "one upper char\n" +
                        "one lower char" +
                        "one special char\n" +
                        "one numeric number";
            }
            return null;
        });

        FormUtils.attachValidator(confirmPassword, confirmPasswordText -> {
            String passwordText = password.getText().toString();

            if (confirmPasswordText.length() <= 0) {
                return "enter your confirm password";
            } else if (!passwordText.equals(confirmPasswordText)) {
                return "your password and confirm password should match";
            }
            return null;
        });
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

    private void showError(String message) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
    }
}
