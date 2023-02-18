package com.example.foodplanner.features.common.repositories;

import com.example.foodplanner.features.common.entities.FavouriteMealEntity;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.local.FavouriteMealDAO;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteRepository {

    private final RepositoryFetchDelegate<String, MealItem, MealItemEntity> fetchFavouritesDelegate;
    private final FavouriteMealDAO favouriteMealDAO;

    public FavouriteRepository(FavouriteMealDAO favouriteMealDAO,
                               BaseMapper<MealItem, MealItemEntity> mapper) {
        this.favouriteMealDAO = favouriteMealDAO;
        fetchFavouritesDelegate = new RepositoryFetchDelegate<>(
                null,
                favouriteMealDAO::getAllActive,
                null,
                null,
                mapper
        );
    }

    public Flowable<List<MealItem>> getAllForUser(String userId) {
        return fetchFavouritesDelegate.fetch(userId).map(mealItems -> mealItems.stream()
                .map(mealItem -> mealItem.setFavourite(true))
                .collect(Collectors.toList())
        );
    }

    public Completable addToFavourite(MealItem mealItem, String userId) {
        FavouriteMealEntity entity = new FavouriteMealEntity();
        entity.active = true;
        entity.mealId = mealItem.getId();
        entity.userId = userId;
        return favouriteMealDAO.insertAll(entity).subscribeOn(Schedulers.io());
    }

    public Completable removeFromFavourite(MealItem mealItem, String userId) {
        FavouriteMealEntity entity = new FavouriteMealEntity();
        entity.active = false;
        entity.mealId = mealItem.getId();
        entity.userId = userId;
        return favouriteMealDAO.updateAll(entity).subscribeOn(Schedulers.io());
    }
}
