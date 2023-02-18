package com.example.foodplanner.features.mealdetails.views;

import com.example.foodplanner.features.common.models.Meal;

import java.util.List;

public interface MealDetailsView {

        void updateMealDetails(Meal meal);
        void onLoadFailure(Throwable error);

}
