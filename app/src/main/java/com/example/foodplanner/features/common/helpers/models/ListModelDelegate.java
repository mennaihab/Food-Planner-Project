package com.example.foodplanner.features.common.helpers.models;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListModelDelegate<T extends Parcelable> {
    protected final Flowable<List<T>> data;
    private final String key;

    public ListModelDelegate(Bundle savedInstanceState, String key, Flowable<List<T>> source) {
        this.key = key;
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            data = Flowable.just(savedInstanceState.getParcelableArrayList(key));
        } else {
            data = source.subscribeOn(Schedulers.io());
        }
    }

    public Flowable<List<T>> getData() {
        return data;
    }

    public void saveInstance(Bundle outBundle) {
        List<T> ingredients = this.data.onErrorReturnItem(Collections.emptyList()).blockingSingle();
        if (!ingredients.isEmpty()) {
            outBundle.putParcelableArrayList(key, new ArrayList<>(ingredients));
        }
    }
}
