package com.example.foodplanner.core.utils;

import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import java.util.Objects;

public abstract class NavigationUtils {
    public static <T> void setResult(View view, String key, T result) {
        Objects.requireNonNull(Navigation.findNavController(view).getPreviousBackStackEntry())
                .getSavedStateHandle().set(key, result);
    }

    public static <T> void maybeSetResult(View view, String key, T result) {
        NavBackStackEntry entry = Navigation.findNavController(view).getPreviousBackStackEntry();
        if (entry != null) {
            entry.getSavedStateHandle().set(key, result);
        }
    }

    public static <T> LiveData<T> getResult(View view, String key) {
        return Objects.requireNonNull(Navigation.findNavController(view).getCurrentBackStackEntry())
                .getSavedStateHandle().getLiveData(key);
    }

    public static void navigateUp(View view) {
        Navigation.findNavController(view).navigateUp();
    }
}
