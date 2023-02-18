package com.example.foodplanner.features.launching.views;

import android.content.Context;
import android.content.Intent;
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
import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.features.common.services.AuthenticationManager;
import com.example.foodplanner.features.common.services.SettingsManager;
import com.example.foodplanner.features.common.views.WindowPainter;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;

public class LaunchingFragment extends Fragment {

    private WindowPainter windowPainter;
    private Disposable disposable;

    public LaunchingFragment() {
        super(R.layout.fragment_launching);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        windowPainter = (WindowPainter) context;
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

        AuthenticationManager authManager = FoodPlannerApplication.from(requireContext()).getAuthenticationManager();
        SettingsManager settingsManager = FoodPlannerApplication.from(requireContext()).getSettingsManager();

        disposable = Single.zip(
                authManager.getCurrentUserObservable().firstOrError(),
                Single.timer(3000, TimeUnit.MILLISECONDS),
                (user, ignored) -> user).observeOn(AndroidSchedulers.mainThread()).subscribe(user -> {
            if (user.isPresent() && !user.get().isAnonymous()) {
                Navigation.findNavController(view)
                        .navigate(LaunchingFragmentDirections.actionGlobalToHome());
            } else if (Objects.equals(settingsManager.hasShownLanding(), true)){
                Navigation.findNavController(view)
                        .navigate(LaunchingFragmentDirections.actionLaunchingToAuthentication());
            } else {
                Navigation.findNavController(view)
                        .navigate(LaunchingFragmentDirections.actionLaunchingToLanding());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null) disposable.dispose();
    }
}