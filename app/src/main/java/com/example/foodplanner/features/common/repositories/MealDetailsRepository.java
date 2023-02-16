package com.example.foodplanner.features.common.repositories;

import com.example.foodplanner.features.common.entities.MealDetailsEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.local.MealDetailsDAO;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MealDetailsRepository {

    private final RepositoryFetchDelegate<Void, Meal, MealDetailsEntity> fetchDelegate;
    public MealDetailsRepository(MealRemoteService mealRemoteService,
                          MealDetailsDAO mealDetailsDAO,
                          BaseMapper<Meal, MealDetailsEntity> mapper) {

        fetchDelegate = new RepositoryFetchDelegate<>(
                a -> mealRemoteService.listMealDetails(),
                b -> mealDetailsDAO.getAll(),
                mealDetailsDAO::insertAll,
                mapper
        );
    }
    public Flowable<List<Meal>> getAll() {
        return fetchDelegate.fetch(null);
    }
}

