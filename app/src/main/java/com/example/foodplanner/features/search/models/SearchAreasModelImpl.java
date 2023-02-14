package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.remote.MealRemoteService;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Flowable;

public class SearchAreasModelImpl implements SearchAreasModel {
    private static final String AREAS = "AREAS";

    private final SearchFilterModelDelegate<Area> delegate;
    public SearchAreasModelImpl(Bundle savedInstanceState, MealRemoteService mealRemoteService) {
        delegate = new SearchFilterModelDelegate<>(savedInstanceState, AREAS, mealRemoteService.listAreas());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        delegate.saveInstance(outBundle);
    }

    @Override
    public Flowable<List<String>> getAreas() {
        return delegate.data.map(areas -> areas.stream().map(Area::getName).collect(Collectors.toList()));
    }

}
