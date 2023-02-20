package com.example.foodplanner.features.plan.adapters;

import com.example.foodplanner.features.common.helpers.ItemClickListener;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;

import java.time.LocalDate;

public interface PlanClickListener extends PlanMealClickListener {
    void onAddItem(LocalDate date);
}
