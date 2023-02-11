package com.example.foodplanner.features.search.presenters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.features.search.models.SearchCategoriesModel;
import com.example.foodplanner.features.search.views.SearchCategoriesView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchCategoriesPresenter implements LifecycleEventObserver {

    private final SearchCategoriesView view;
    private final SearchCategoriesModel categoriesModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public SearchCategoriesPresenter(LifecycleOwner lifecycleOwner, SearchCategoriesView view, SearchCategoriesModel categoriesModel) {
        this.view = view;
        this.categoriesModel = categoriesModel;
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
        disposable.add(categoriesModel.getCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateCategories, view::onLoadFailure));
    }

    private void close() {
        disposable.dispose();
    }

    public void saveInstance(Bundle outState) {
        categoriesModel.saveInstance(outState);
    }
}