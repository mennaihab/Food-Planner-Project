package com.example.foodplanner.features.favourites.views;


import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

public interface FavouritesView {
    void updateFavourites(List<FavouriteMealItem> products);
    void onLoadFailure(Throwable error);
    void onFavouriteSuccess(FavouriteMealItem mealItem);
    void onFavouriteFailure(FavouriteMealItem mealItem, Throwable error);
}
