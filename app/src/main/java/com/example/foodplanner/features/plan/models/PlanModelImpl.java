package com.example.foodplanner.features.plan.models;

import android.os.Bundle;

import com.example.foodplanner.core.utils.UserUtils;
import com.example.foodplanner.features.common.helpers.MealCalenderHelper;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;
import com.example.foodplanner.features.common.repositories.PlanDayArguments;
import com.example.foodplanner.features.common.repositories.PlanRepository;
import com.example.foodplanner.features.common.services.AuthenticationManager;
import com.example.foodplanner.features.common.helpers.CalendarPermissionHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.BehaviorSubject;


public class PlanModelImpl implements PlanModel {
    private static final String PLAN = "PLAN";
    private final BehaviorSubject<Map<LocalDate, List<PlanMealItem>>> source;
    private final AuthenticationManager authenticationManager;
    private final PlanRepository planRepository;
    private final List<PlanMealItem> latestData = new ArrayList<>();

    public PlanModelImpl(Bundle savedInstanceState,
                         AuthenticationManager authenticationManager,
                         PlanRepository planRepository,
                         LocalDate weekStart) {
        this.authenticationManager = authenticationManager;
        this.planRepository = planRepository;
        Flowable<List<PlanMealItem>> source = authenticationManager.getCurrentUserObservable()
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap(user -> {
                    String userId = UserUtils.getUserId(user);
                    if (userId == null) {
                        return Flowable.error(new Exception("You have to be logged in.")); // TODO
                    }
                    return planRepository.getAllWeekForUser(new PlanDayArguments(userId, weekStart, weekStart.plusDays(6)));
                }).doOnNext(mealItems -> {
                    latestData.clear();
                    latestData.addAll(mealItems);
                });

        if (savedInstanceState != null && savedInstanceState.containsKey(PLAN)) {
            latestData.addAll(savedInstanceState.getParcelableArrayList(PLAN));
            source = source.startWithItem(latestData);
        }
        this.source = source
                .map(list -> list.stream()
                        .collect(Collectors.groupingBy(PlanMealItem::getDay)))
                .toObservable().subscribeWith(BehaviorSubject.create());
    }

    @Override
    public void saveInstance(Bundle outBundle) {
        if (!latestData.isEmpty())
            outBundle.putParcelableArrayList(PLAN, new ArrayList<>());
    }

    @Override
    public Single<PlanMealItem> addPlanMeal(MealItem mealItem, LocalDate date) {
        String userId = UserUtils.getUserId(authenticationManager.getCurrentUser());
        if (userId == null) {
            return Single.error(new Exception("You have to be logged in to access your plan"));
        }
        return planRepository.addToPlan(mealItem, userId, date);
    }

    @Override
    public Single<PlanMealItem> removePlanMeal(PlanMealItem item) {
        String userId = UserUtils.getUserId(authenticationManager.getCurrentUser());
        if (userId == null) {
            return Single.error(new Exception("You have to be logged in to access your plan"));
        }
        return planRepository.removeFromPlan(item);
    }

    @Override
    public Flowable<Map<LocalDate, List<PlanMealItem>>> getDayMeals() {
        return source.toFlowable(BackpressureStrategy.LATEST);
    }
}