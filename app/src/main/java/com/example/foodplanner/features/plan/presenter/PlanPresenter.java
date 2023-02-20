package com.example.foodplanner.features.plan.presenter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.plan.models.PlanModel;
import com.example.foodplanner.features.plan.views.WeekFragmentView;

import java.time.LocalDate;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlanPresenter implements LifecycleEventObserver {

    private final WeekFragmentView view;
    private final PlanModel planModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public PlanPresenter( WeekFragmentView view, PlanModel planModel) {
        this.view = view;
        this.planModel = planModel;
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
        disposable.add(planModel.getDayMeals()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::updatePlan, view::onLoadFailure));
    }

    public void addPlanMeal(MealItem mealItem, LocalDate date) {
        disposable.add(planModel.addPlanMeal(mealItem,date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::onItemAdded, (e) -> view.onAddFailure(mealItem,e)));
    }

    private void close(LifecycleOwner source) {
        source.getLifecycle().removeObserver(this);
        disposable.clear();

    }

    public void saveInstance(Bundle outState) {
        planModel.saveInstance(outState);
    }


}