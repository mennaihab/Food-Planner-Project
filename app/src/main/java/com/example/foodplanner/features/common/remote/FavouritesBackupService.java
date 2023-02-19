package com.example.foodplanner.features.common.remote;

import androidx.annotation.NonNull;

import com.example.foodplanner.core.utils.FirestoreUtils;
import com.example.foodplanner.features.common.helpers.RemoteListWrapper;
import com.example.foodplanner.features.common.helpers.RemoteMealListWrapper;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface FavouritesBackupService {
    Flowable<RemoteListWrapper<FavouriteMealItem>> getAllForUser(@NonNull String userId);

    Completable insertForUser(@NonNull String userId, FavouriteMealItem... items);

    Completable insertForUser(@NonNull String userId, List<FavouriteMealItem> items);
}
