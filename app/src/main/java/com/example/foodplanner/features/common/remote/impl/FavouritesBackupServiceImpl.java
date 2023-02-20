package com.example.foodplanner.features.common.remote.impl;

import androidx.annotation.NonNull;

import com.example.foodplanner.core.utils.FirestoreUtils;
import com.example.foodplanner.features.common.helpers.RemoteListWrapper;
import com.example.foodplanner.features.common.helpers.RemoteMealListWrapper;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.FavouritesBackupService;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class FavouritesBackupServiceImpl implements FavouritesBackupService {
    private static final String USERS_COLLECTION = "Users";
    private static final String FAVOURITES_COLLECTION = "Favourites";
    private final FirebaseFirestore firestore;

    public FavouritesBackupServiceImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Flowable<RemoteListWrapper<FavouriteMealItem>> getAllForUser(@NonNull String userId) {
        return FirestoreUtils.toFlowableList(firestore.collection(USERS_COLLECTION)
                .document(userId).collection(FAVOURITES_COLLECTION), FavouriteMealItem.class)
                .map(RemoteMealListWrapper::new);
    }

    @Override
    public Completable insertForUser(@NonNull String userId, FavouriteMealItem... items) {
        return FirestoreUtils.insertAll(firestore.collection(USERS_COLLECTION)
                .document(userId).collection(FAVOURITES_COLLECTION), FavouriteMealItem::getId, items);
    }

    @Override
    public Completable insertForUser(@NonNull String userId, List<FavouriteMealItem> items) {
        return FirestoreUtils.insertAll(firestore.collection(USERS_COLLECTION)
                .document(userId).collection(FAVOURITES_COLLECTION), FavouriteMealItem::getId, items);
    }
}
