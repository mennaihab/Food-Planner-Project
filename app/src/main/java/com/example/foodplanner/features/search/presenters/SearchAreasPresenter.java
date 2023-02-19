package com.example.foodplanner.features.search.presenters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.features.search.models.SearchAreasModel;
import com.example.foodplanner.features.search.views.SearchAreasView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchAreasPresenter implements LifecycleEventObserver {

    private final SearchAreasView view;
    private final SearchAreasModel areasModel;
    private Disposable disposable;

    public SearchAreasPresenter(SearchAreasView view, SearchAreasModel areasModel) {
        this.view = view;
        this.areasModel = areasModel;
    }

    public void init(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            init();
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            close(source);
        }
    }

    private void init() {
        disposable = areasModel.getAreas()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateAreas, view::onLoadFailure);
    }

    private void close(LifecycleOwner source) {
        source.getLifecycle().removeObserver(this);
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void saveInstance(Bundle outState) {
        areasModel.saveInstance(outState);
    }
}