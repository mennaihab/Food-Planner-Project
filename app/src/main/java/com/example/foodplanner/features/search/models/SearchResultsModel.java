package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.search.helpers.SearchCriteria;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface SearchResultsModel {
    /**
     * @return A flowable of optional list of items: empty optional means new data are being loaded.
     */
    Flowable<Optional<List<FavouriteMealItem>>> getResults();
    SearchCriteria getCriteria();
    void filter(SearchCriteria criteria);
    Single<FavouriteMealItem> updateFavourite(FavouriteMealItem mealItem);
    void saveInstance(Bundle outBundle);
    void close();

}
