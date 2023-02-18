package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Ingredient;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface SearchIngredientsModel {
    Single<List<Ingredient>> getIngredients();
    void saveInstance(Bundle outBundle);

}
