package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.helpers.models.ListModelDelegate;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.repositories.AreaRepository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class SearchAreasModelImpl implements SearchAreasModel {
    private static final String AREAS = "AREAS";

    private final ListModelDelegate<Area> delegate;
    public SearchAreasModelImpl(Bundle savedInstanceState, AreaRepository areaRepository) {
        delegate = new ListModelDelegate<>(savedInstanceState, AREAS, areaRepository.getAll().firstOrError());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        delegate.saveInstance(outBundle);
    }

    @Override
    public Single<List<String>> getAreas() {
        return delegate.getData().map(areas -> areas.stream().map(Area::getName).collect(Collectors.toList()));
    }

}
