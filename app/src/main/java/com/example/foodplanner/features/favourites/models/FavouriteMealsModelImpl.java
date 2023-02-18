package com.example.foodplanner.features.favourites.models;

import android.os.Bundle;

import com.example.foodplanner.core.utils.UserUtils;
import com.example.foodplanner.features.common.helpers.models.ListModelDelegate;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.services.AuthenticationManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class FavouriteMealsModelImpl implements FavouriteMealsModel {
    private static final String FAVOURITES = "FAVOURITES";
    private final Flowable<List<MealItem>> source;

    private final List<MealItem> latestData = new ArrayList<>();

    private final AuthenticationManager authenticationManager;
    private final FavouriteRepository favouriteRepository;

    public FavouriteMealsModelImpl(Bundle savedInstanceState,
                                   AuthenticationManager authenticationManager,
                                   FavouriteRepository favouriteRepository) {
        this.authenticationManager = authenticationManager;
        this.favouriteRepository = favouriteRepository;
        Flowable<List<MealItem>> source = authenticationManager.getCurrentUserObservable()
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap(user -> {
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
            source = source.startWithItem(latestData);
        }

        this.source = source;
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        if (!latestData.isEmpty()) {
            outBundle.putParcelableArrayList(FAVOURITES, new ArrayList<>(latestData));
        }
    }

    @Override
    public Single<MealItem> updateFavourite(MealItem mealItem) {
        String userId = UserUtils.getUserId(authenticationManager.getCurrentUser());
        if (userId == null) {
            return Single.error(new Exception("You have to be logged in.")); // TODO
        }
        if (mealItem.isFavourite()) {
            return favouriteRepository.removeFromFavourite(mealItem, userId).andThen(Single.create(emitter -> {
                emitter.onSuccess(mealItem.setFavourite(false));
            }));
        } else {
            return favouriteRepository.addToFavourite(mealItem, userId).andThen(Single.create(emitter -> {
                emitter.onSuccess(mealItem.setFavourite(true));
            }));
        }
    }

    @Override
    public Flowable<List<MealItem>> getMeals() {
        return source;
    }

}
