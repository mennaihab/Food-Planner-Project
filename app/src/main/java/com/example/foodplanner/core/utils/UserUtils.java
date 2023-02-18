package com.example.foodplanner.core.utils;

import com.google.firebase.auth.FirebaseUser;

import java.util.Optional;

public abstract class UserUtils {
    public static boolean isPresent(Optional<FirebaseUser> user) {
        return user.isPresent() && !user.get().isAnonymous();
    }
    public static String getUserId(Optional<FirebaseUser> user) {
        if (user.isPresent() && !user.get().isAnonymous()) {
            return user.get().getUid();
        }
        return null;
    }
}
