package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Ingredient;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface SearchIngredientsModel {
    Flowable<List<Ingredient>> getIngredients();
    void saveInstance(Bundle outBundle);

}
