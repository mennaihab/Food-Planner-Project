package com.example.foodplanner.features.mealofday.models;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.example.foodplanner.core.utils.UserUtils;
import com.example.foodplanner.features.common.helpers.models.ListModelDelegate;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
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
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MealOfDayModelImpl implements MealOfDayModel {
    private static final String TAG = "MealOfDayModelImpl";
    private static final String MEAL_OF_DAY = "MEAL_OF_DAY";

    protected final BehaviorSubject<FavouriteMealItem> data;
    private Optional<MealItem> meal = Optional.empty();;
    private final AuthenticationManager authenticationManager;
    private final FavouriteRepository favouriteRepository;

    public MealOfDayModelImpl(Bundle savedInstanceState,
                              AuthenticationManager authenticationManager,
                              MealItemRepository mealItemRepository,
                              FavouriteRepository favouriteRepository) {
        this.authenticationManager = authenticationManager;
        this.favouriteRepository = favouriteRepository;
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
                String userId = UserUtils.getUserId(user);
                return favouriteRepository.isFavouriteForUser(userId, meal).map(isFavourite -> Pair.create(userId, isFavourite));
            } else {
                return Flowable.just(Pair.<String, Boolean>create(null, false));
            }
        }).map(isFavourite -> new FavouriteMealItem(isFavourite.first, isFavourite.second, meal)))
                .toObservable().subscribeWith(BehaviorSubject.create());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        meal.ifPresent(mealItem -> outBundle.putParcelable(MEAL_OF_DAY, mealItem));
    }

    @Override
    public Flowable<FavouriteMealItem> getMeal() {
        return data.toFlowable(BackpressureStrategy.LATEST);
    }


    @Override
    public Single<FavouriteMealItem> updateFavourite() {
        FavouriteMealItem meal = data.getValue();
        Single<FavouriteMealItem> result;
        if (meal != null) {
            String userId = UserUtils.getUserId(authenticationManager.getCurrentUser());
            if (userId == null) {
                return Single.error(new Exception("You have to be logged in.")); // TODO
            }
            if (meal.isFavourite()) {
                result = favouriteRepository.removeFromFavourite(meal, userId);
            } else {
                result = favouriteRepository.addToFavourite(meal, userId);
            }
        } else {
            result = Single.error(new Exception("No item selected"));
        }
        return result;
    }
}
