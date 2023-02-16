package com.example.foodplanner.features.mealdetails.presenter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.features.mealdetails.models.MealDetailsModel;
import com.example.foodplanner.features.mealdetails.views.MealDetailsView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MealDetailsPresenter implements LifecycleEventObserver {

    private final MealDetailsView view;
    private final MealDetailsModel mealDetailsModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MealDetailsPresenter(LifecycleOwner lifecycleOwner, MealDetailsView view, MealDetailsModel mealDetailsModel) {
        this.view = view;
        this.mealDetailsModel = mealDetailsModel;
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
        disposable.add(mealDetailsModel.getMealDetails()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> view.updateMealDetails(meals.get(0)), view::onLoadFailure));
    }

    private void close() {
        disposable.dispose();
    }

    public void saveInstance(Bundle outState) {
        mealDetailsModel.saveInstance(outState);
    }
}
