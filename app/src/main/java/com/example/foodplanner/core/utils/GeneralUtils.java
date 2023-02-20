package com.example.foodplanner.core.utils;

public abstract class GeneralUtils {
    public static String getErrorMessage(Throwable error) {
        String errorMessage = error.getLocalizedMessage();
        if (errorMessage == null) errorMessage = error.getMessage();
        if (errorMessage == null) return "An error occurred.";
        return errorMessage;
    }
}
