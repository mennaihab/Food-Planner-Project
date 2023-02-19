package com.example.foodplanner.features.mealofday.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealOfDayModel {
    Flowable<FavouriteMealItem> getMeal();
    void saveInstance(Bundle outBundle);
}
