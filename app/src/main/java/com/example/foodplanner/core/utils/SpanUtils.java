package com.example.foodplanner.core.utils;

import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.foodplanner.core.helpers.TextSpan;

import java.util.function.Consumer;

public abstract class SpanUtils {

    public static Object createClickableSpan(Consumer<View> onClick) {
        return createClickableSpan(onClick, true);
    }

    public static Object createClickableSpan(Consumer<View> onClick, boolean underline) {
        return new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                onClick.accept(textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(underline);
            }
        };
    }

    public static SpannableString createSpannable(String text, TextSpan... spans) {
        SpannableString spannableString = new SpannableString(text);
        for (TextSpan span: spans) {
            int end = span.end == null ? text.length() : span.end;
            spannableString.setSpan(span.span, span.start, end, span.spanType);
        }
        return spannableString;
    }
}
