package com.example.foodplanner.features.launching.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.views.WindowPainter;

public class LaunchingFragment extends Fragment {

    private WindowPainter windowPainter;

    public LaunchingFragment() {
        super(R.layout.fragment_launching);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tv = view.findViewById(R.id.app_name_tv);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setRepeatMode(Animation.REVERSE);
        fadeIn.setRepeatCount(Animation.INFINITE);
        fadeIn.setDuration(2000);

        tv.startAnimation(fadeIn);

        windowPainter.setStatusBarColor(Color.TRANSPARENT);
        windowPainter.setStatusBarVisibility(false);

        view.postDelayed(() -> {
            Navigation.findNavController(view)
                    .navigate(LaunchingFragmentDirections.actionLaunchingToLanding());
        }, 5000);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        windowPainter = (WindowPainter) context;
    }

}