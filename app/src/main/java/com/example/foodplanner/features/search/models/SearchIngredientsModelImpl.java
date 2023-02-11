package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.remote.MealRemoteService;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class SearchIngredientsModelImpl extends SearchModelBase<Ingredient> implements SearchIngredientsModel {
    private static final String INGREDIENTS = "INGREDIENTS";
    public SearchIngredientsModelImpl(Bundle savedInstanceState, MealRemoteService ingredientService) {
        super(savedInstanceState, INGREDIENTS, ingredientService.listIngredients());
    }

    @Override
    public Flowable<List<Ingredient>> getIngredients() {
        return data;
    }

}
