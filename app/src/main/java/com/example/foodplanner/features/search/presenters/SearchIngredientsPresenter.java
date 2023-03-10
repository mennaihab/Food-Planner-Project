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
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchIngredientsPresenter implements LifecycleEventObserver {

    private final SearchIngredientsView view;
    private final SearchIngredientsModel ingredientsModel;
    private Disposable disposable;

    public SearchIngredientsPresenter(SearchIngredientsView view, SearchIngredientsModel ingredientsModel) {
        this.view = view;
        this.ingredientsModel = ingredientsModel;
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
        disposable = ingredientsModel.getIngredients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateIngredients, view::onLoadFailure);
    }

    private void close(LifecycleOwner source) {
        source.getLifecycle().removeObserver(this);
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void saveInstance(Bundle outState) {
        ingredientsModel.saveInstance(outState);
    }
}