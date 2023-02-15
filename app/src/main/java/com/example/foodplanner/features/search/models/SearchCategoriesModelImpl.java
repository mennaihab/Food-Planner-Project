package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Category;
import com.example.foodplanner.features.common.repositories.CategoryRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class SearchCategoriesModelImpl implements SearchCategoriesModel {
    private static final String CATEGORIES = "CATEGORIES";
    private final SearchFilterModelDelegate<Category> delegate;
    public SearchCategoriesModelImpl(Bundle savedInstanceState, CategoryRepository ingredientService) {
        delegate = new SearchFilterModelDelegate<>(savedInstanceState, CATEGORIES, ingredientService.getAll());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        delegate.saveInstance(outBundle);
    }

    @Override
    public Flowable<List<Category>> getCategories() {
        return delegate.data;
    }

}
