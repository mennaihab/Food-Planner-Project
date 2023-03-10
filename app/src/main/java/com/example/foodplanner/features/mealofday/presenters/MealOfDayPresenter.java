package com.example.foodplanner.features.mealofday.presenters;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.mealofday.models.MealOfDayModel;
import com.example.foodplanner.features.mealofday.views.MealOfDayView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MealOfDayPresenter implements LifecycleEventObserver {

    private final MealOfDayView view;
    private final MealOfDayModel mealOfDayModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MealOfDayPresenter(MealOfDayView view, MealOfDayModel mealOfDayModel) {
        this.view = view;
        this.mealOfDayModel = mealOfDayModel;
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
        disposable.add(mealOfDayModel.getMeal()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updateMeal, view::onLoadFailure));
    }

    public void updateFavourite() {
        disposable.add(mealOfDayModel.updateFavourite().subscribe());
    }

    private void close(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().removeObserver(this);
            disposable.clear();
    }

    public void saveInstance(Bundle outState) {
        mealOfDayModel.saveInstance(outState);
    }


}