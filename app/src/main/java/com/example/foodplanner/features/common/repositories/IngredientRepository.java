package com.example.foodplanner.features.common.repositories;

import com.example.foodplanner.features.common.entities.IngredientEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.local.IngredientDAO;
import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class IngredientRepository {
    private final RepositoryFetchDelegate<Void, Ingredient, IngredientEntity> fetchDelegate;
    public IngredientRepository(MealRemoteService mealRemoteService,
                                IngredientDAO ingredientDAO,
                                BaseMapper<Ingredient, IngredientEntity> mapper) {

        fetchDelegate = new RepositoryFetchDelegate<>(
                a -> mealRemoteService.listIngredients(),
                b -> ingredientDAO.getAll(),
                null,
                (arg, list) -> ingredientDAO.insertAll(list),
                mapper
        );
    }

    public Flowable<List<Ingredient>> getAll() {
        return fetchDelegate.fetch(null);
    }
}
