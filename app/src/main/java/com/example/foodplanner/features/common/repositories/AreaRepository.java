package com.example.foodplanner.features.common.repositories;

import com.example.foodplanner.features.common.entities.AreaEntity;
import com.example.foodplanner.features.common.helpers.RemoteMealWrapper;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.local.AreaDAO;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class AreaRepository {

    private final RepositoryFetchDelegate<Void, Area, AreaEntity> fetchDelegate;
    public AreaRepository(MealRemoteService mealRemoteService,
                          AreaDAO areaDAO,
                          BaseMapper<Area, AreaEntity> mapper) {

        fetchDelegate = new RepositoryFetchDelegate<>(
                a -> mealRemoteService.listAreas(),
                b -> areaDAO.getAll(),
                areaDAO::insertAll,
                mapper
        );
    }

    public Flowable<List<Area>> getAll() {
        return fetchDelegate.fetch(null);
    }
}
