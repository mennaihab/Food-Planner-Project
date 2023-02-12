package com.example.foodplanner.features.search.models;

import android.os.Bundle;
import android.os.Parcelable;

import com.example.foodplanner.features.common.helpers.RemoteModelWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchModelDelegate<T extends Parcelable> {
    protected final Flowable<List<T>> data;
    private final String key;

    public SearchModelDelegate(Bundle savedInstanceState, String key, Single<? extends RemoteModelWrapper<T>> remoteSource) {
        this.key = key;
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            data = Flowable.just(savedInstanceState.getParcelableArrayList(key));
        } else {
            data = Flowable.fromSingle(
                    remoteSource
                    .subscribeOn(Schedulers.io())
                    .map(RemoteModelWrapper::getItems)
            );
        }
    }

    public void saveInstance(Bundle outBundle) {
        List<T> ingredients = this.data.onErrorReturnItem(Collections.emptyList()).blockingSingle();
        if (!ingredients.isEmpty()) {
            outBundle.putParcelableArrayList(key, new ArrayList<>(ingredients));
        }
    }
}
