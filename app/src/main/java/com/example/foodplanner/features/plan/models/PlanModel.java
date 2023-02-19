package com.example.foodplanner.features.plan.models;

import android.os.Bundle;

import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface PlanModel {

    Flowable<Map<LocalDate, List<PlanMealItem>>> getDayMeals();
    void saveInstance(Bundle outBundle);

}
