package com.example.foodplanner.features.favourites.presenters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.features.authentication.helpers.AuthenticationHelper;
import com.example.foodplanner.features.authentication.presenters.AuthenticationPresenter;
import com.example.foodplanner.features.authentication.services.EmailAuthService;
import com.example.foodplanner.features.authentication.services.FacebookAuthService;
import com.example.foodplanner.features.authentication.services.GoogleAuthService;
import com.example.foodplanner.features.authentication.services.GuestAuthService;
import com.example.foodplanner.features.authentication.views.AuthenticationView;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.helpers.mappers.FavouriteMealMapper;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.impl.FavouritesBackupServiceImpl;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.common.views.OperationSink;
import com.example.foodplanner.features.favourites.models.FavouriteMealsModel;
import com.example.foodplanner.features.favourites.models.FavouriteMealsModelImpl;
import com.example.foodplanner.features.favourites.views.FavouritesView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void updateFavourite(FavouriteMealItem mealItem) {
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