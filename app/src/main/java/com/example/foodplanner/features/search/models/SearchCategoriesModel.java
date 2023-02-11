package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Category;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface SearchCategoriesModel {
    Flowable<List<Category>> getCategories();
    void saveInstance(Bundle outBundle);
}
