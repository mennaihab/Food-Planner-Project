package com.example.foodplanner.features.authentication.views;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.foodplanner.R;
import com.google.android.material.internal.TextWatcherAdapter;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        super(R.layout.fragment_signup);
    }

    // TODO: use string resources

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText name = view.findViewById(R.id.name_edit_text);
        EditText email = view.findViewById(R.id.email_edit_text);
        EditText password = view.findViewById(R.id.password_edit_text);
        EditText confirmPassword = view.findViewById(R.id.confirm_password_edit_text);

        attachValidator(name, nameText -> {
            if (nameText.length() <= 0) {
                return "enter your name";
            }
            return null;
        });

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

        attachValidator(password, passwordText -> {
            String regex = "\n" +
                    "^(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$\n";
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

        attachValidator(confirmPassword, confirmPasswordText -> {
            String passwordText = password.getText().toString();

            if (confirmPasswordText.length() <= 0) {
                return "enter your confirm password";
            } else if (!passwordText.equals(confirmPasswordText)) {
                return "your password and confirm password should match";
            }
            return null;
        });
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
