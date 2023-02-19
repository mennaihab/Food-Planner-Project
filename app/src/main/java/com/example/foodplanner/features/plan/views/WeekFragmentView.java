package com.example.foodplanner.features.plan.views;

import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;

import java.util.List;

public interface WeekFragmentView {
    void updatePlan(List<PlanMealItem> products);
    void onLoadFailure(Throwable error);
    void onFavouriteSuccess(MealItem mealItem);
    void onFavouriteFailure(MealItem mealItem, Throwable error);
}
