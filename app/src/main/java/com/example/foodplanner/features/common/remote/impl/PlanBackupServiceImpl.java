package com.example.foodplanner.features.common.remote.impl;

import androidx.annotation.NonNull;

import com.example.foodplanner.core.utils.FirestoreUtils;
import com.example.foodplanner.features.common.helpers.RemoteListWrapper;
import com.example.foodplanner.features.common.helpers.RemoteMealListWrapper;
import com.example.foodplanner.features.common.models.PlanMealItem;
import com.example.foodplanner.features.common.remote.PlanBackupService;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class PlanBackupServiceImpl implements PlanBackupService {
    private static final String USERS_COLLECTION = "Users";
    private static final String FAVOURITES_COLLECTION = "Plans";
    private final FirebaseFirestore firestore;

    public PlanBackupServiceImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Flowable<RemoteListWrapper<PlanMealItem>> getAllForUser(@NonNull String userId) {
        return FirestoreUtils.toFlowableList(firestore.collection(USERS_COLLECTION)
                .document(userId).collection(FAVOURITES_COLLECTION), PlanMealItem.class)
                .map(RemoteMealListWrapper::new);
    }

    @Override
    public Completable insertForUser(@NonNull String userId, PlanMealItem... items) {
        return FirestoreUtils.insertAll(firestore.collection(USERS_COLLECTION)
                .document(userId).collection(FAVOURITES_COLLECTION), PlanMealItem::getPersistenceId, items);
    }

    @Override
    public Completable insertForUser(@NonNull String userId, List<PlanMealItem> items) {
        return FirestoreUtils.insertAll(firestore.collection(USERS_COLLECTION)
                .document(userId).collection(FAVOURITES_COLLECTION), PlanMealItem::getPersistenceId, items);
    }
}
