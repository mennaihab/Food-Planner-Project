package com.example.foodplanner.features.mealofday.views;


import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

public interface MealOfDayView {
    void updateMeal(MealItem meal);
    void onLoadFailure(Throwable error);
}
