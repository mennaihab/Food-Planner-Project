package com.example.foodplanner.features.search.presenters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.features.search.models.SearchAreasModel;
import com.example.foodplanner.features.search.models.SearchIngredientsModel;
import com.example.foodplanner.features.search.views.SearchAreasView;
import com.example.foodplanner.features.search.views.SearchIngredientsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchAreasPresenter implements LifecycleEventObserver {

    private final SearchAreasView view;
    private final SearchAreasModel areasModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public SearchAreasPresenter(LifecycleOwner lifecycleOwner, SearchAreasView view, SearchAreasModel areasModel) {
        this.view = view;
        this.areasModel = areasModel;
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            init();
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            close();
        }
    }

    private void init() {
        disposable.add(areasModel.getAreas()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateAreas, view::onLoadFailure));
    }

    private void close() {
        disposable.dispose();
    }

    public void saveInstance(Bundle outState) {
        areasModel.saveInstance(outState);
    }
}