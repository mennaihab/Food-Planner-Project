package com.example.foodplanner.features.favourites.helpers;

import com.example.foodplanner.features.common.helpers.ItemClickListener;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;

public interface FavouriteClickListener extends ItemClickListener<FavouriteMealItem> {
    void onFavourite(FavouriteMealItem item);
}
