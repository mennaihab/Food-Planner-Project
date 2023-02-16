package com.example.foodplanner.features.mealdetails.models;

import android.os.Bundle;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.repositories.MealDetailsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsModelImpl implements MealDetailsModel {
    protected final Flowable<List<Meal>> data;
    private static final String MEALDETAILS = "MEALDETAILS";

    public MealDetailsModelImpl(Bundle savedInstanceState, MealDetailsRepository mealDetailsService) {
        if (savedInstanceState != null && savedInstanceState.containsKey(MEALDETAILS)) {
            data = Flowable.just(savedInstanceState.getParcelableArrayList(MEALDETAILS));
        } else {
            data = mealDetailsService.getAll().subscribeOn(Schedulers.io());
        }
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        List<Meal> mealDetails = this.data.onErrorReturnItem(Collections.emptyList()).blockingSingle();
        if (!mealDetails.isEmpty()) {
            outBundle.putParcelableArrayList(MEALDETAILS, new ArrayList<>(mealDetails));
        }
    }
    @Override
    public Flowable<List<Meal>> getMealDetails() {
        return data;
    }
}
