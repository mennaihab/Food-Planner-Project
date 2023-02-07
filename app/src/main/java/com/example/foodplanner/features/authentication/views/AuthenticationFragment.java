package com.example.foodplanner.features.authentication.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.views.WindowPainter;

public class AuthenticationFragment extends Fragment {

    private WindowPainter windowPainter;

    public AuthenticationFragment() {
        super(R.layout.fragment_authentication);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        windowPainter = (WindowPainter) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        windowPainter.clearStatusBarColor();
    }
}