package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.core.utils.UserUtils;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.repositories.MealItemRepository;
import com.example.foodplanner.features.common.services.AuthenticationManager;
import com.example.foodplanner.features.search.helpers.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class SearchResultsModelImpl implements SearchResultsModel {
    private static final String SEARCH_RESULTS = "SEARCH_RESULTS";
    private static final String SEARCH_CRITERIA = "SEARCH_CRITERIA";
    private final BehaviorSubject<Optional<List<MealItem>>> searchResults;
    private final AuthenticationManager authenticationManager;
    private final MealItemRepository mealItemRepository;

    private final FavouriteRepository favouriteRepository;
    private Disposable lastOperation;
    private SearchCriteria criteria;

    public SearchResultsModelImpl(Bundle savedInstanceState,
                                  SearchCriteria criteria,
                                  AuthenticationManager authenticationManager,
                                  MealItemRepository mealItemRepository,
                                  FavouriteRepository favouriteRepository) {
        this.authenticationManager = authenticationManager;
        this.mealItemRepository = mealItemRepository;
        this.favouriteRepository = favouriteRepository;
        final Optional<List<MealItem>> data;
        if (savedInstanceState != null && savedInstanceState.containsKey(SEARCH_CRITERIA)) {
            this.criteria = savedInstanceState.getParcelable(SEARCH_CRITERIA);
        } else {
            this.criteria = criteria;
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(SEARCH_RESULTS)) {
            data = Optional.of(savedInstanceState.getParcelableArrayList(SEARCH_RESULTS));
        } else {
            data = Optional.empty();
        }
        searchResults = BehaviorSubject.createDefault(data);
        if (!data.isPresent()) {
            filter(criteria);
        }
    }

    @Override
    public Flowable<Optional<List<MealItem>>> getResults() {
        Flowable<List<String>> favourites = authenticationManager
                .getCurrentUserObservable()
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap(user -> favouriteRepository
                        .getAllForUser(UserUtils.getUserId(user))
                        .map(items -> items.stream()
                                .map(MealItem::getId)
                                .collect(Collectors.toList()))
                );
        return searchResults.toFlowable(BackpressureStrategy.LATEST).flatMap(mealItems -> {
            if (mealItems.isPresent()) {
                return favourites.map(favouritesIds -> {
                   return mealItems.map(meals -> meals.stream().map(meal -> {
                       return meal.setFavourite(favouritesIds.contains(meal.getId()));
                   }).collect(Collectors.toList()));
                });
            }
            return Flowable.just(mealItems);
        });
    }

    @Override
    public SearchCriteria getCriteria() {
        return criteria;
    }

    @Override
    public void filter(SearchCriteria criteria) {
        setCurrent(mealItemRepository.filter(criteria));
        this.criteria = criteria;
    }

    private void setCurrent(Flowable<List<MealItem>> newSource) {
        dispose();
        Observable<Optional<List<MealItem>>> observable = newSource.map(Optional::of)
                .toObservable()
                .startWith(Single.just(Optional.empty()))
                .subscribeOn(Schedulers.io());
        lastOperation = observable.subscribe(searchResults::onNext, searchResults::onError);
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        Optional<List<MealItem>> data = searchResults.getValue();
        if (data != null && data.isPresent()) {
            outBundle.putParcelableArrayList(SEARCH_RESULTS, new ArrayList<>(data.get()));
        }
        outBundle.putParcelable(SEARCH_CRITERIA, criteria);
    }

    @Override
    public Single<MealItem> updateFavourite(MealItem mealItem) {
        String userId = UserUtils.getUserId(authenticationManager.getCurrentUser());
        if (userId == null) {
            return Single.error(new Exception("You have to be logged in.")); // TODO
        }
        if (mealItem.isFavourite()) {
            return favouriteRepository.removeFromFavourite(mealItem, userId).andThen(Single.create(emitter -> {
                emitter.onSuccess(mealItem.setFavourite(false));
            }));
        } else {
            return favouriteRepository.addToFavourite(mealItem, userId).andThen(Single.create(emitter -> {
                emitter.onSuccess(mealItem.setFavourite(true));
            }));
        }
    }

    @Override
    public void close() {
        dispose();
    }

    private void dispose() {
        if (lastOperation != null) {
            lastOperation.dispose();
            lastOperation = null;
        }
    }
}
