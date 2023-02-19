package com.example.foodplanner.features.mealofday.models;

import android.os.Bundle;
import android.util.Log;

import com.example.foodplanner.core.utils.UserUtils;
import com.example.foodplanner.features.common.helpers.models.ListModelDelegate;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.repositories.AreaRepository;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.repositories.MealItemRepository;
import com.example.foodplanner.features.common.services.AuthenticationManager;
import com.example.foodplanner.features.search.models.SearchAreasModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealOfDayModelImpl implements MealOfDayModel {
    private static final String MEAL_OF_DAY = "MEAL_OF_DAY";

    protected final Flowable<MealItem> data;
    private Optional<MealItem> meal = Optional.empty();;

    public MealOfDayModelImpl(Bundle savedInstanceState,
                              AuthenticationManager authenticationManager,
                              MealItemRepository mealItemRepository,
                              FavouriteRepository favouriteRepository) {

        if (savedInstanceState != null && savedInstanceState.containsKey(MEAL_OF_DAY)) {
            meal = Optional.of(savedInstanceState.getParcelable(MEAL_OF_DAY));
        }

        final Single<MealItem> data = Single.fromCallable(() -> meal).flatMap(optionalMeal -> {
            if (optionalMeal.isPresent()) {
                return Single.just(optionalMeal.get());
            } else {
                return mealItemRepository.randomMeal()
                        .firstOrError()
                        .doOnSuccess(mealItem -> {
                    meal = Optional.of(mealItem);
                });
            }
        });

        Flowable<Optional<FirebaseUser>> userStream = authenticationManager
                .getCurrentUserObservable()
                .toFlowable(BackpressureStrategy.LATEST);

        this.data = data.toFlowable().flatMap(meal -> userStream.flatMap(user -> {
            if (UserUtils.isPresent(user)) {
                return favouriteRepository.isFavouriteForUser(UserUtils.getUserId(user), meal);
            } else {
                return Flowable.just(false);
            }
        }).map(meal::setFavourite)).subscribeOn(Schedulers.io());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        meal.ifPresent(mealItem -> outBundle.putParcelable(MEAL_OF_DAY, mealItem));
    }

    @Override
    public Flowable<MealItem> getMeal() {
        return data;
    }

}
