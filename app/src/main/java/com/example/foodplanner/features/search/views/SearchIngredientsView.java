package com.example.foodplanner.features.search.views;


import com.example.foodplanner.features.common.models.Ingredient;

import java.util.List;

public interface SearchIngredientsView {
    void updateIngredients(List<Ingredient> products);
    void onLoadFailure(Throwable error);
}
