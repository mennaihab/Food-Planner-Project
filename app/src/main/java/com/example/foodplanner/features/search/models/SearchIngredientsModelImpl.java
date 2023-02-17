package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.helpers.models.ListModelDelegate;
import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.repositories.IngredientRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class SearchIngredientsModelImpl implements SearchIngredientsModel {
    private static final String INGREDIENTS = "INGREDIENTS";
    private final ListModelDelegate<Ingredient> delegate;
    public SearchIngredientsModelImpl(Bundle savedInstanceState, IngredientRepository ingredientService) {
        delegate = new ListModelDelegate<>(savedInstanceState, INGREDIENTS, ingredientService.getAll());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        delegate.saveInstance(outBundle);
    }

    @Override
    public Flowable<List<Ingredient>> getIngredients() {
        return delegate.getData();
    }

}
