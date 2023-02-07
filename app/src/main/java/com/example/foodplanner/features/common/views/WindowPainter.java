package com.example.foodplanner.features.common.views;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public interface WindowPainter {
    void setToolbarVisibility(boolean visible);
    void setBottomNavVisibility(boolean visible);
    void setStatusBarVisibility(boolean visible);

    void setStatusBarColor(@ColorInt int color);

    void setToolbarColor(@ColorInt int color);
    void clearToolbarColor();

    void clearStatusBarColor();
}
