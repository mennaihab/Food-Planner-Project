package com.example.foodplanner.features.search.presenters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.search.helpers.SearchCriteria;
import com.example.foodplanner.features.search.models.SearchIngredientsModel;
import com.example.foodplanner.features.search.models.SearchResultsModel;
import com.example.foodplanner.features.search.views.SearchIngredientsView;
import com.example.foodplanner.features.search.views.SearchResultsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchResultsPresenter implements LifecycleEventObserver {
    private final SearchResultsView view;
    private final SearchResultsModel searchResultsModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public SearchResultsPresenter(LifecycleOwner lifecycleOwner, SearchResultsView view, SearchResultsModel searchResultsModel) {
        this.view = view;
        this.searchResultsModel = searchResultsModel;
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

    public void filter(SearchCriteria criteria) {
        searchResultsModel.filter(criteria);
    }

    public void updateFavourite(MealItem mealItem) {
        disposable.add(searchResultsModel.updateFavourite(mealItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::onFavouriteSuccess, (e) -> view.onFavouriteFailure(mealItem, e)));
    }

    private void init() {
        disposable.add(searchResultsModel.getResults()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateResults, view::onLoadFailure));
    }

    private void close() {
        disposable.dispose();
        searchResultsModel.close();
    }

    public void saveInstance(Bundle outState) {
        searchResultsModel.saveInstance(outState);
    }
}