package com.example.foodplanner.features.common.repositories;

import com.example.foodplanner.features.common.entities.MealEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.local.MealDetailsDAO;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryItemDelegate;

import io.reactivex.rxjava3.core.Flowable;

public class MealDetailsRepository {

    private final RepositoryItemDelegate<String, Meal, MealEntity> fetchDelegate;

    public MealDetailsRepository(MealRemoteService mealRemoteService,
                                 MealDetailsDAO mealDetailsDAO,
                                 BaseMapper<Meal, MealEntity> mapper) {

        fetchDelegate = new RepositoryItemDelegate<>(
                mealRemoteService::listMealDetails,
                id -> mealDetailsDAO.getById(id).map(mealEntity -> {
                    return mealEntity.orElseThrow(Exception::new); // TODO
                }),
                null,
                mealDetailsDAO::insertAll,
                mapper
        );
    }

    public Flowable<Meal> getById(String id) {
        return fetchDelegate.fetch(id);
    }
}

