package com.example.foodplanner.features.favourites.presenters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.favourites.models.FavouriteMealsModel;
import com.example.foodplanner.features.favourites.views.FavouritesView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FavouritesPresenter implements LifecycleEventObserver {

    private final FavouritesView view;
    private final FavouriteMealsModel favouritesModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public FavouritesPresenter(FavouritesView view, FavouriteMealsModel favouritesModel) {
        this.view = view;
        this.favouritesModel = favouritesModel;
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

    public void updateFavourite(MealItem mealItem) {
        disposable.add(favouritesModel.updateFavourite(mealItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::onFavouriteSuccess, (e) -> view.onFavouriteFailure(mealItem, e)));
    }

    private void init() {
        disposable.add(favouritesModel.getMeals()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateFavourites, view::onLoadFailure));
    }

    private void close(LifecycleOwner source) {
        source.getLifecycle().removeObserver(this);
        disposable.clear();
    }

    public void saveInstance(Bundle outState) {
        favouritesModel.saveInstance(outState);
    }
}