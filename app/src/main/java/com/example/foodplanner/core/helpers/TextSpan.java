package com.example.foodplanner.core.helpers;

import android.text.SpannableString;
import android.text.Spanned;

public class TextSpan {
    public final Object span;
    public final int start;
    public final Integer end;
    public final int spanType;

    public TextSpan(Object span, int start, Integer end, int spanType) {
        this.span = span;
        this.start = start;
        this.end = end;
        this.spanType = spanType;
    }

    public static TextSpan of(Object span, int start, int end, int spanType) {
        return new TextSpan(span, start, end, spanType);
    }

    public static TextSpan of(Object span, int start) {
        return new TextSpan(span, start, null, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    public static TextSpan of(Object span) {
        return new TextSpan(span, 0, null, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }
}
