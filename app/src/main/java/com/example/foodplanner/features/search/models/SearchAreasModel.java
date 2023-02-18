package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface SearchAreasModel {
    Single<List<String>> getAreas();
    void saveInstance(Bundle outBundle);

}
