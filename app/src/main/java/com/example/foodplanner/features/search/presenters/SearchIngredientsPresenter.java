package com.example.foodplanner.features.search.presenters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


import com.example.foodplanner.features.search.models.SearchIngredientsModel;
import com.example.foodplanner.features.search.views.SearchIngredientsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchIngredientsPresenter implements LifecycleEventObserver {

    private final SearchIngredientsView view;
    private final SearchIngredientsModel ingredientsModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public SearchIngredientsPresenter(LifecycleOwner lifecycleOwner, SearchIngredientsView view, SearchIngredientsModel ingredientsModel) {
        this.view = view;
        this.ingredientsModel = ingredientsModel;
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
        disposable.add(ingredientsModel.getIngredients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateIngredients, view::onLoadFailure));
    }

    private void close() {
        disposable.dispose();
    }

    public void saveInstance(Bundle outState) {
        ingredientsModel.saveInstance(outState);
    }
}