package com.example.foodplanner.core.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.foodplanner.R;

public abstract class ViewUtils {
    private static final int placeholder = R.drawable.ic_downloading_24;
    private static final int error = R.drawable.ic_error_outline_24;

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void loadImageInto(String imageUrl, ImageView view) {
        Glide.with(view)
                .asBitmap()
                .load(imageUrl)
                .placeholder(placeholder)
                .error(error)
                .into(view);
    }

    public static void loadImageInto(String imageUrl, ImageView view, RequestListener<Bitmap> requestListener) {
        Glide.with(view)
                .asBitmap()
                .load(imageUrl)
                .placeholder(placeholder)
                .error(error)
                .listener(requestListener)
                .into(view);
    }

    public static void loadImageInto(String imageUrl, ImageView view, int size, RequestListener<Bitmap> requestListener) {
        Glide.with(view)
                .asBitmap()
                .load(imageUrl)
                .placeholder(placeholder)
                .error(error)
                .listener(requestListener)
                .override(size)
                .into(view);
    }

    public static void loadImageInto(String imageUrl, ImageView view, Target<Bitmap> target) {
        Glide.with(view)
                .asBitmap()
                .load(imageUrl)
                .placeholder(placeholder)
                .error(error)
                .into(target);
    }

    public static void loadImageInto(String imageUrl, View view) {
        Glide.with(view)
                .load(imageUrl)
                .placeholder(placeholder)
                .error(error)
                .into(new RequestFutureTarget<Drawable>(view.getWidth(), view.getHeight()) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
    }

    public static void loadImageInto(@DrawableRes int res, View view) {
        Glide.with(view)
                .load(res)
                .placeholder(placeholder)
                .error(error)
                .into(new RequestFutureTarget<Drawable>(view.getWidth(), view.getHeight()) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
    }
}
