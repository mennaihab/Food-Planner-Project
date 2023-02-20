package com.example.foodplanner.features.common.repositories;

import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.entities.PlanDayEntity;
import com.example.foodplanner.features.common.helpers.mappers.PlanMealMapper;
import com.example.foodplanner.features.common.local.PlanDayDAO;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanRepository {

    private final RepositoryFetchDelegate<PlanDayArguments, PlanMealItem, PlanDayEntity.Full> fetchPlanDelegate;
    private final PlanDayDAO planDayDAO;

    public PlanRepository(PlanDayDAO planDayDAO,
                               PlanMealMapper mapper) {
        this.planDayDAO = planDayDAO;
        fetchPlanDelegate = new RepositoryFetchDelegate<>(
                null,
                a -> planDayDAO.getAll(a.getUserId(),a.getStartDay(),a.getEndDay()),
                null,
                null,
                mapper
        );
    }

    public Flowable<List<PlanMealItem>> getAllWeekForUser( PlanDayArguments planDayArguments) {
        return  fetchPlanDelegate.fetch(planDayArguments);
    }


    public Completable addToPlan(MealItem mealItem, String userId,LocalDate day) {
        PlanDayEntity entity = new PlanDayEntity();
        entity.active = true;
        entity.mealId = mealItem.getId();
        entity.userId = userId;
        entity.day = day;
        return planDayDAO.insertAll(entity).subscribeOn(Schedulers.io());
    }

    public Completable removeFromPlan(MealItem mealItem, String userId,LocalDate day) {
        PlanDayEntity entity = new PlanDayEntity();
        entity.active = false;
        entity.mealId = mealItem.getId();
        entity.userId = userId;
        entity.day = day;
        return planDayDAO.updateAll(entity).subscribeOn(Schedulers.io());
    }
}
