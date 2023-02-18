package com.example.foodplanner.features.favourites.helpers;

import com.example.foodplanner.features.common.helpers.ItemClickListener;
import com.example.foodplanner.features.common.models.MealItem;

public interface FavouriteClickListener extends ItemClickListener<MealItem> {
    void onFavourite(MealItem item);
}
