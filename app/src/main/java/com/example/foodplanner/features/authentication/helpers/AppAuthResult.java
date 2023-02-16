package com.example.foodplanner.features.authentication.helpers;

import com.google.firebase.auth.FirebaseUser;

public abstract class AppAuthResult {
    private final Provider provider;

    protected AppAuthResult(Provider provider) {
        this.provider = provider;
    }

    public Provider getProvider() {
        return provider;
    }

    public static class Success extends AppAuthResult {
        private final FirebaseUser user;

        public Success(Provider provider, FirebaseUser user) {
            super(provider);
            this.user = user;
        }

        public FirebaseUser getUser() {
            return user;
        }
    }

    public static class Cancellation extends AppAuthResult {
        public Cancellation(Provider provider) {
            super(provider);
        }
    }

    public static class Failure extends AppAuthResult {
        private final Throwable error;

        public Failure(Provider provider, Throwable error) {
            super(provider);
            this.error = error;
        }

        public Throwable getError() {
            return error;
        }
    }

    public enum Provider {
        FACEBOOK,
        GOOGLE,
        EMAIL,
        GUEST,
    }
}
