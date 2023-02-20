package com.example.foodplanner.features.mealdetails.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.repositories.MealDetailsRepository;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsModelImpl implements MealDetailsModel {
    private static final String MEAL_DETAILS = "MEAL_DETAILS";

    protected final Single<Meal> data;
    private Meal meal;

    public MealDetailsModelImpl(Bundle savedInstanceState,
                                String mealId,
                                MealDetailsRepository mealDetailsService) {
        if (savedInstanceState != null && savedInstanceState.containsKey(MEAL_DETAILS)) {
            meal = savedInstanceState.getParcelable(MEAL_DETAILS);
            data = Single.just(meal);
        } else {
            data = mealDetailsService.getById(mealId)
                    .subscribeOn(Schedulers.io())
                    .firstOrError().doOnSuccess(meal -> {
                this.meal = meal;
            });
        }
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        if (meal != null) {
            outBundle.putParcelable(MEAL_DETAILS, meal);
        }
    }

    @Override
    public Single<Meal> getMealDetails() {
        return data;
    }

}
