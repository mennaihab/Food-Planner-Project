package com.example.foodplanner.features.common.remote;

import androidx.annotation.NonNull;

import com.example.foodplanner.features.common.helpers.RemoteListWrapper;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface PlanBackupService {
    Flowable<RemoteListWrapper<PlanMealItem>> getAllForUser(@NonNull String userId);

    Completable insertForUser(@NonNull String userId, PlanMealItem... items);

    Completable insertForUser(@NonNull String userId, List<PlanMealItem> items);
}
