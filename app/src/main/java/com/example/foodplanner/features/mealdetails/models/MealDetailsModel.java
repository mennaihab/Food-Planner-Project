package com.example.foodplanner.features.mealdetails.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface MealDetailsModel {
    Flowable<List<Meal>> getMealDetails();
    void saveInstance(Bundle outBundle);
}
