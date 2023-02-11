package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.helpers.RemoteModelWrapper;
import com.example.foodplanner.features.common.models.Category;
import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;

import io.reactivex.rxjava3.core.Flowable;

public class SearchCategoriesModelImpl extends SearchModelBase<Category> implements SearchCategoriesModel {
    private static final String CATEGORIES = "CATEGORIES";
    public SearchCategoriesModelImpl(Bundle savedInstanceState, MealRemoteService ingredientService) {
        super(savedInstanceState, CATEGORIES, ingredientService.listCategories());
    }

    @Override
    public Flowable<List<Category>> getCategories() {
        return data;
    }

}
