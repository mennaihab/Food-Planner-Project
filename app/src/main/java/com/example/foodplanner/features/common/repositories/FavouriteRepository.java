package com.example.foodplanner.features.common.repositories;

import android.util.Log;

import com.example.foodplanner.features.common.entities.FavouriteMealEntity;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.helpers.mappers.IdentityMapper;
import com.example.foodplanner.features.common.local.FavouriteMealDAO;
import com.example.foodplanner.features.common.local.MealItemDAO;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.FavouritesBackupService;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteRepository {
    private static final String TAG = "FavouriteRepository";

    private final RepositoryFetchDelegate<String, FavouriteMealItem, FavouriteMealEntity.Full> fetchFavouritesDelegate;
    private final RepositoryFetchDelegate<String, FavouriteMealItem, FavouriteMealEntity.Full> remoteBackupDelegate;
    private final RepositoryFetchDelegate<String, String, String> fetchFavouritesIdsDelegate;
    private final FavouriteMealDAO favouriteMealDAO;
    private final MealItemDAO mealItemDAO;
    private final FavouritesBackupService favouritesBackupService;

    public FavouriteRepository(FavouriteMealDAO favouriteMealDAO,
                               MealItemDAO mealItemDAO,
                               FavouritesBackupService favouritesBackupService,
                               BaseMapper<FavouriteMealItem, FavouriteMealEntity.Full> mapper) {
        this.favouriteMealDAO = favouriteMealDAO;
        this.mealItemDAO = mealItemDAO;
        this.favouritesBackupService = favouritesBackupService;
        fetchFavouritesDelegate = new RepositoryFetchDelegate<>(
                null,
                favouriteMealDAO::getAllActive,
                favouritesBackupService::insertForUser,
                null,
                mapper
        );
        remoteBackupDelegate = new RepositoryFetchDelegate<>(
                (userId) -> favouritesBackupService.getAllForUser(userId).firstOrError(),
                null,
                null,
                (args, list) -> saveRemoteBackup(list),
                mapper
        );
        fetchFavouritesIdsDelegate = new RepositoryFetchDelegate<>(
                null,
                favouriteMealDAO::getAllActiveIds,
                null,
                null,
                new IdentityMapper<>(String.class)
        );
    }

    private Completable saveRemoteBackup(List<FavouriteMealEntity.Full> list) {
        List<FavouriteMealEntity> favouriteMealEntities = list.stream()
                .map(FavouriteMealEntity.Full::toEntity)
                .collect(Collectors.toList());
        List<MealItemEntity> mealItemEntities = list.stream()
                .map(item -> item.meal)
                .collect(Collectors.toList());
        return mealItemDAO
                .insertAllNew(mealItemEntities)
                .subscribeOn(Schedulers.io())
                .andThen(favouriteMealDAO
                        .insertAll(favouriteMealEntities));
    }

    public Flowable<List<FavouriteMealItem>> getAllForUser(String userId) {
        return fetchFavouritesDelegate.fetch(userId).subscribeOn(Schedulers.io());
    }

    public Flowable<List<String>> getAllIdsForUser(String userId) {
        return fetchFavouritesIdsDelegate.fetch(userId).subscribeOn(Schedulers.io());
    }

    public Flowable<Boolean> isFavouriteForUser(String userId, MealItem meal) {
        return favouriteMealDAO.getById(userId, meal.getId())
                .map(optionalItem -> optionalItem.isPresent() && optionalItem.get().active).subscribeOn(Schedulers.io());
    }

    public Single<FavouriteMealItem> addToFavourite(FavouriteMealItem mealItem, String userId) {
        FavouriteMealEntity entity = new FavouriteMealEntity();
        entity.active = true;
        entity.mealId = mealItem.getId();
        entity.userId = userId;
        FavouriteMealItem item = mealItem.copy();
        item.setFavourite(true);
        item.setUserId(userId);
        return favouriteMealDAO.insertAll(entity)
                .andThen(backupToRemote(userId, Collections.singletonList(item)))
                .andThen(Single.just(item))
                .subscribeOn(Schedulers.io());
    }

    public Single<FavouriteMealItem> removeFromFavourite(FavouriteMealItem mealItem, String userId) {
        FavouriteMealEntity entity = new FavouriteMealEntity();
        entity.active = false;
        entity.mealId = mealItem.getId();
        entity.userId = userId;
        FavouriteMealItem item = mealItem.copy();
        item.setFavourite(false);
        item.setUserId(userId);
        return favouriteMealDAO
                .updateAll(entity)
                .andThen(backupToRemote(userId, Collections.singletonList(item)))
                .andThen(Single.just(item))
                .subscribeOn(Schedulers.io());
    }

    private Completable backupToRemote(String userId, List<FavouriteMealItem> items) {
        Log.d(TAG, "backupToRemote: backing up");
        return favouritesBackupService.insertForUser(userId, items).subscribeOn(Schedulers.io());
    }

    public Completable getBackupFromRemote(String userId) {
        Log.d(TAG, "getBackupFromRemote: backing up");
        return remoteBackupDelegate.fetch(userId).subscribeOn(Schedulers.io()).firstOrError().ignoreElement();
    }
}
