package com.example.foodplanner.features.plan.views;

import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WeekFragmentView {
     void updatePlan(Map<LocalDate,List<PlanMealItem>> products) ;
    void onLoadFailure(Throwable error);
    void onItemAdded(PlanMealItem planMealItem);
    void onItemRemoved(PlanMealItem planMealItem);
    void onAddFailure(MealItem mealItem, Throwable error);
    void onRemoveFailure(PlanMealItem mealItem, Throwable error);

}
