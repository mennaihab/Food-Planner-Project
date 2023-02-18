package com.example.foodplanner.core.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.foodplanner.core.helpers.FormFieldValidator;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class FormUtils {

    @SuppressLint("RestrictedApi")
    public static void attachValidator(EditText editText, Function<String, String> validator) {
        FormFieldValidator validatorTag = () -> {
            String error = validator.apply(editText.getText().toString());
            if (editText instanceof TextInputEditText) {
                TextInputLayout layout = getTextInputLayout((TextInputEditText) editText);
                Objects.requireNonNull(layout).setError(error);
            } else {
                editText.setError(error);
            }
            return error;
        };
        editText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(@NonNull Editable s) {
                validatorTag.validate();
            }
        });
        editText.setTag(validatorTag);
    }

    private static TextInputLayout getTextInputLayout(TextInputEditText editText) {
        ViewParent parent = editText.getParent();
        while (parent instanceof View) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    public static FormFieldValidator getValidator(View field) {
        Object tag = field.getTag();
        if (tag instanceof FormFieldValidator) {
            return (FormFieldValidator) tag;
        }
        return null;
    }

    public static String[] validateForm(View... fields) {
        return Arrays.stream(fields)
                .map(FormUtils::getValidator)
                .filter(Objects::nonNull)
                .map(FormFieldValidator::validate)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }
}
