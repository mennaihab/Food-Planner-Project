package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.search.helpers.SearchCriteria;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Flowable;

public interface SearchResultsModel {
    /**
     * @return A flowable of optional list of items: empty optional means new data are being loaded.
     */
    Flowable<Optional<List<MealItem>>> getResults();
    void filter(SearchCriteria criteria);
    void saveInstance(Bundle outBundle);
    void close();
}
