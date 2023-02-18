package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.helpers.models.ListModelDelegate;
import com.example.foodplanner.features.common.models.Category;
import com.example.foodplanner.features.common.repositories.CategoryRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class SearchCategoriesModelImpl implements SearchCategoriesModel {
    private static final String CATEGORIES = "CATEGORIES";
    private final ListModelDelegate<Category> delegate;
    public SearchCategoriesModelImpl(Bundle savedInstanceState, CategoryRepository ingredientService) {
        delegate = new ListModelDelegate<>(savedInstanceState, CATEGORIES, ingredientService.getAll().firstOrError());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        delegate.saveInstance(outBundle);
    }

    @Override
    public Single<List<Category>> getCategories() {
        return delegate.getData();
    }

}
