package com.example.foodplanner.features.authentication.views;

import com.example.foodplanner.features.authentication.helpers.AppAuthResult;

public interface AuthenticationView {
    void onAuthResult(AppAuthResult authResult);
}
