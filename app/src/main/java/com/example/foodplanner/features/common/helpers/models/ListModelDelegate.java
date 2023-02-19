package com.example.foodplanner.features.common.helpers.models;

import android.os.Bundle;
import android.os.Parcelable;

import com.example.foodplanner.features.common.models.MealItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListModelDelegate<T extends Parcelable> {

    protected final Single<List<T>> data;
    private final String key;
    private Optional<List<T>> latestData = Optional.empty();

    public ListModelDelegate(Bundle savedInstanceState,
                             String key,
                             Single<List<T>> source) {
        this.key = key;
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            latestData = Optional.of(savedInstanceState.getParcelableArrayList(key));
        }

        data = Single.fromCallable(() -> latestData).flatMap(optionalMeal -> {
            if (optionalMeal.isPresent()) {
                return Single.just(optionalMeal.get());
            } else {
                return source.doOnSuccess(dataList -> latestData = Optional.of(dataList));
            }
        }).subscribeOn(Schedulers.io());
    }

    public Single<List<T>> getData() {
        return data;
    }

    public void saveInstance(Bundle outBundle) {
        latestData.ifPresent(data -> outBundle.putParcelableArrayList(key, new ArrayList<>(data)));
    }
}
