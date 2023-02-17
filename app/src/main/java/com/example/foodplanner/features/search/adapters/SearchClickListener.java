package com.example.foodplanner.features.search.adapters;

import com.example.foodplanner.features.common.helpers.ItemClickListener;
import com.example.foodplanner.features.common.models.MealItem;

public interface SearchClickListener extends ItemClickListener<MealItem> {
    void onFavourite(MealItem item);
}
