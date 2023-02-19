package com.example.foodplanner.features.common.repositories;

import com.example.foodplanner.features.common.entities.CategoryEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.local.CategoryDAO;
import com.example.foodplanner.features.common.models.Category;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class CategoryRepository {

    private final RepositoryFetchDelegate<Void, Category, CategoryEntity> fetchDelegate;
    public CategoryRepository(MealRemoteService mealRemoteService,
                              CategoryDAO categoryDAO,
                              BaseMapper<Category, CategoryEntity> mapper) {

        fetchDelegate = new RepositoryFetchDelegate<>(
                a -> mealRemoteService.listCategories(),
                b -> categoryDAO.getAll(),
                null,
                (arg, list) -> categoryDAO.insertAll(list),
                mapper
        );
    }

    public Flowable<List<Category>> getAll() {
        return fetchDelegate.fetch(null);
    }
}
