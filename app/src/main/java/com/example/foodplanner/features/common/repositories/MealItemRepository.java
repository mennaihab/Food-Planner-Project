package com.example.foodplanner.features.common.repositories;

import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.local.MealItemDAO;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;
import com.example.foodplanner.features.search.helpers.SearchCriteria;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MealItemRepository {
    private final RepositoryFetchDelegate<String, MealItem, MealItemEntity> fetchByNameDelegate;
    private final RepositoryFetchDelegate<String, MealItem, MealItemEntity> fetchByCategoryDelegate;
    private final RepositoryFetchDelegate<String, MealItem, MealItemEntity> fetchByAreaDelegate;
    private final RepositoryFetchDelegate<String, MealItem, MealItemEntity> fetchByIngredientDelegate;

    public MealItemRepository(MealRemoteService mealRemoteService,
                              MealItemDAO mealItemDAO,
                              BaseMapper<MealItem, MealItemEntity> mapper) {
        fetchByNameDelegate = new RepositoryFetchDelegate<>(
                mealRemoteService::searchByName,
                mealItemDAO::getAllWhereNameContains,
                null,
                mealItemDAO::insertAll,
                mapper
        );
        fetchByCategoryDelegate = new RepositoryFetchDelegate<>(
                mealRemoteService::searchByCategory,
                null,
                null,
                mealItemDAO::insertAll,
                mapper
        );
        fetchByAreaDelegate = new RepositoryFetchDelegate<>(
                mealRemoteService::searchByArea,
                null,
                null,
                mealItemDAO::insertAll,
                mapper
        );
        fetchByIngredientDelegate = new RepositoryFetchDelegate<>(
                mealRemoteService::searchByIngredient,
                null,
                null,
                mealItemDAO::insertAll,
                mapper
        );
    }

    public Flowable<List<MealItem>> filter(SearchCriteria criteria) {
        switch (criteria.getType()) {
            case QUERY:
                return fetchByNameDelegate.fetch(criteria.getCriteria());
            case INGREDIENT:
                return fetchByIngredientDelegate.fetch(criteria.getCriteria());
            case AREA:
                return fetchByAreaDelegate.fetch(criteria.getCriteria());
            case CATEGORY:
                return fetchByCategoryDelegate.fetch(criteria.getCriteria());
            default:
                throw new RuntimeException("Missing switch case " + criteria.getType());
        }
    }
}
