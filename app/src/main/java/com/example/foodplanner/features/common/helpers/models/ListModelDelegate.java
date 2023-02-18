package com.example.foodplanner.features.common.helpers.models;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListModelDelegate<T extends Parcelable> {
    protected final Single<List<T>> data;
    private final List<T> latestData = new ArrayList<>();
    private final String key;

    public ListModelDelegate(Bundle savedInstanceState,
                             String key,
                             Single<List<T>> source) {
        this.key = key;
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            latestData.addAll(savedInstanceState.getParcelableArrayList(key));
            data = Single.just(latestData);
        } else {
            data = source.subscribeOn(Schedulers.io()).doOnSuccess(data -> {
                latestData.clear();
                latestData.addAll(data);
            });
        }
    }

    public Single<List<T>> getData() {
        return data;
    }

    public void saveInstance(Bundle outBundle) {
        if (!latestData.isEmpty()) {
            outBundle.putParcelableArrayList(key, new ArrayList<>(latestData));
        }
    }
}
