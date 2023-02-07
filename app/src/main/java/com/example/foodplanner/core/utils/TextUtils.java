package com.example.foodplanner.core.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.example.foodplanner.core.helpers.TextSpan;

import java.util.function.Consumer;

public abstract class TextUtils {
    public static void makeClickable(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.GRAY);
    }
}
