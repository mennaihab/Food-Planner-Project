package com.example.foodplanner.features.favourites.views;


import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

public interface FavouritesView {
    void updateFavourites(List<MealItem> products);
    void onLoadFailure(Throwable error);
    void onFavouriteSuccess(MealItem mealItem);
    void onFavouriteFailure(MealItem mealItem, Throwable error);
}
