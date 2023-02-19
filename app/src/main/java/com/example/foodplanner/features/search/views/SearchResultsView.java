package com.example.foodplanner.features.search.views;


import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;
import java.util.Optional;

public interface SearchResultsView {
    void updateResults(Optional<List<FavouriteMealItem>> results);
    void onLoadFailure(Throwable error);
    void onFavouriteSuccess(FavouriteMealItem mealItem);
    void onFavouriteFailure(FavouriteMealItem mealItem, Throwable error);
}
