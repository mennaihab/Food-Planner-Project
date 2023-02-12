package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.remote.MealRemoteService;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class SearchIngredientsModelImpl implements SearchIngredientsModel {
    private static final String INGREDIENTS = "INGREDIENTS";
    private final SearchModelDelegate<Ingredient> delegate;
    public SearchIngredientsModelImpl(Bundle savedInstanceState, MealRemoteService ingredientService) {
        delegate = new SearchModelDelegate<>(savedInstanceState, INGREDIENTS, ingredientService.listIngredients());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        delegate.saveInstance(outBundle);
    }

    @Override
    public Flowable<List<Ingredient>> getIngredients() {
        return delegate.data;
    }

}
