package com.example.foodplanner.features.favourites.models;

import android.os.Bundle;
import android.util.Log;

import com.example.foodplanner.core.utils.UserUtils;
import com.example.foodplanner.features.common.helpers.models.ListModelDelegate;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.services.AuthenticationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class FavouriteMealsModelImpl implements FavouriteMealsModel {
    private static final String TAG = "FavouriteMealsModelImpl";
    private static final String FAVOURITES = "FAVOURITES";
    private final List<FavouriteMealItem> latestData = new ArrayList<>();
    private final AuthenticationManager authenticationManager;
    private final FavouriteRepository favouriteRepository;

    private final BehaviorSubject<List<FavouriteMealItem>> favouritesHolder;

    public FavouriteMealsModelImpl(Bundle savedInstanceState,
                                   AuthenticationManager authenticationManager,
                                   FavouriteRepository favouriteRepository) {
        this.authenticationManager = authenticationManager;
        this.favouriteRepository = favouriteRepository;
        Flowable<List<FavouriteMealItem>> source = authenticationManager.getCurrentUserObservable()
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap(user -> {
                    Log.d(TAG, "FavouriteMealsModelImpl: " + user);
                    String userId = UserUtils.getUserId(user);
                    if (userId == null) {
                        return Flowable.error(new Exception("You have to be logged in.")); // TODO
                    }
                    return favouriteRepository.getAllForUser(userId);
                }).doOnNext(mealItems -> {
                    latestData.clear();
                    latestData.addAll(mealItems);
                });
        if (savedInstanceState != null && savedInstanceState.containsKey(FAVOURITES)) {
            latestData.addAll(savedInstanceState.getParcelableArrayList(FAVOURITES));
            source = source.startWithItem(latestData).debounce(200, TimeUnit.MILLISECONDS);
        }
        favouritesHolder = source.toObservable().subscribeWith(BehaviorSubject.create());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        if (!latestData.isEmpty()) {
            outBundle.putParcelableArrayList(FAVOURITES, new ArrayList<>(latestData));
        }
    }

    @Override
    public Single<FavouriteMealItem> updateFavourite(FavouriteMealItem mealItem) {
        String userId = UserUtils.getUserId(authenticationManager.getCurrentUser());
        if (userId == null) {
            return Single.error(new Exception("You have to be logged in.")); // TODO
        }
        if (mealItem.isFavourite()) {
            return favouriteRepository.removeFromFavourite(mealItem, userId);
        } else {
            return favouriteRepository.addToFavourite(mealItem, userId);
        }
    }

    @Override
    public Flowable<List<FavouriteMealItem>> getMeals() {
        return favouritesHolder.toFlowable(BackpressureStrategy.LATEST);
    }

}
