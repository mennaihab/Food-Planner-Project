package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.remote.MealRemoteService;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Flowable;

public class SearchAreasModelImpl extends SearchModelBase<Area> implements SearchAreasModel {
    private static final String AREAS = "AREAS";
    public SearchAreasModelImpl(Bundle savedInstanceState, MealRemoteService mealRemoteService) {
        super(savedInstanceState, AREAS, mealRemoteService.listAreas());
    }

    @Override
    public Flowable<List<String>> getAreas() {
        return data.map(areas -> areas.stream().map(Area::getName).collect(Collectors.toList()));
    }

}
