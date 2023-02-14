package com.example.foodplanner.features.search.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.helpers.RemoteMealWrapper;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.search.helpers.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final MealRemoteService mealService;
    private Disposable lastOperation;
    private SearchCriteria criteria;

    public SearchResultsModelImpl(Bundle savedInstanceState, SearchCriteria criteria, MealRemoteService mealService) {
        this.mealService = mealService;
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
        return searchResults.toFlowable(BackpressureStrategy.LATEST);
    }

    @Override
    public void filter(SearchCriteria criteria) {
        switch (criteria.getType()) {
            case QUERY:
                setCurrent(mealService.searchByName(criteria.getCriteria()));
                break;
            case INGREDIENT:
                setCurrent(mealService.searchByIngredient(criteria.getCriteria()));
                break;
            case AREA:
                setCurrent(mealService.searchByArea(criteria.getCriteria()));
                break;
            case CATEGORY:
                setCurrent(mealService.searchByCategory(criteria.getCriteria()));
                break;
        }
        this.criteria = criteria;
    }

    private void setCurrent(Single<RemoteMealWrapper<MealItem>> newSource) {
        dispose();
        Observable<Optional<List<MealItem>>> observable = newSource.map(list -> {
                    if (list.getItems() != null) return list.getItems();
                    return new ArrayList<MealItem>();
                }).map(Optional::of)
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
