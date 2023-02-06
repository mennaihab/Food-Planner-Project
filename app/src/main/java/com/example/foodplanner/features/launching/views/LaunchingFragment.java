package com.example.foodplanner.features.launching.views;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodplanner.R;

public class LaunchingFragment extends Fragment {

    public LaunchingFragment() {
        super(R.layout.activity_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tv = view.findViewById(R.id.app_name_tv);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);

        tv.startAnimation(fadeIn);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                tv.startAnimation(fadeIn);
                fadeOut.setDuration(2000);
                fadeOut.setFillAfter(true);
                fadeOut.setStartOffset(4200 + fadeIn.getStartOffset());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.postDelayed(() -> {
            // TODO move to landing
        }, 5000);
    }
}