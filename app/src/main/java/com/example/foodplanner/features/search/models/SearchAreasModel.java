package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface SearchAreasModel {
    Flowable<List<String>> getAreas();
    void saveInstance(Bundle outBundle);

}
