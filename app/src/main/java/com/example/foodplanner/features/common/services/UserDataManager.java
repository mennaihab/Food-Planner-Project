package com.example.foodplanner.features.common.services;

import com.example.foodplanner.features.authentication.helpers.AppAuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;

public interface UserDataManager {
    void close();
}
