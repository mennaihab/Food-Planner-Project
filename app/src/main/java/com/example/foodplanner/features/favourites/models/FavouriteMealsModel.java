package com.example.foodplanner.features.favourites.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface FavouriteMealsModel {

    Flowable<List<MealItem>> getMeals();
    void saveInstance(Bundle outBundle);

    Single<MealItem> updateFavourite(MealItem mealItem);
}
