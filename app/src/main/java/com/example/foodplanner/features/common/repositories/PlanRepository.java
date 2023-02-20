package com.example.foodplanner.features.common.repositories;

import android.util.Log;

import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.entities.PlanDayEntity;
import com.example.foodplanner.features.common.helpers.MealCalenderHelper;
import com.example.foodplanner.features.common.helpers.mappers.PlanMealMapper;
import com.example.foodplanner.features.common.local.MealItemDAO;
import com.example.foodplanner.features.common.local.PlanDayDAO;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.models.PlanMealItem;
import com.example.foodplanner.features.common.remote.PlanBackupService;
import com.example.foodplanner.features.common.repositories.delegates.RepositoryFetchDelegate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanRepository {
    private static final String TAG = "PlanRepository";

    private final RepositoryFetchDelegate<PlanDayArguments, PlanMealItem, PlanDayEntity.Full> fetchPlanDelegate;
    private final RepositoryFetchDelegate<String, PlanMealItem, PlanDayEntity.Full> remoteBackupDelegate;
    private final PlanDayDAO planDayDAO;
    private final MealItemDAO mealItemDAO;
    private final PlanBackupService planBackupService;

    private MealCalenderHelper mealCalenderHelper;

    public PlanRepository(PlanDayDAO planDayDAO,
                          MealItemDAO mealItemDAO,
                          PlanBackupService planBackupService,
                          PlanMealMapper mapper) {
        this.planDayDAO = planDayDAO;
        this.mealItemDAO = mealItemDAO;
        this.planBackupService = planBackupService;
        fetchPlanDelegate = new RepositoryFetchDelegate<>(
                null,
                a -> planDayDAO.getAllActive(a.getUserId(), a.getStartDay(), a.getEndDay()),
                null,
                null,
                mapper
        );
        remoteBackupDelegate = new RepositoryFetchDelegate<>(
                (userId) -> planBackupService.getAllForUser(userId).firstOrError(),
                null,
                null,
                (args, list) -> saveRemoteBackup(list),
                mapper
        );
    }

    public void setMealCalenderHelper(MealCalenderHelper mealCalenderHelper) {
        this.mealCalenderHelper = mealCalenderHelper;
    }

    private Completable saveRemoteBackup(List<PlanDayEntity.Full> list) {
        List<PlanDayEntity> favouriteMealEntities = list.stream()
                .map(PlanDayEntity.Full::toEntity)
                .collect(Collectors.toList());
        List<MealItemEntity> mealItemEntities = list.stream()
                .map(item -> item.meal)
                .collect(Collectors.toList());
        return mealItemDAO
                .insertAllNew(mealItemEntities)
                .subscribeOn(Schedulers.io())
                .andThen(planDayDAO
                        .insertAll(favouriteMealEntities));
    }

    public Flowable<List<PlanMealItem>> getAllWeekForUser(PlanDayArguments planDayArguments) {
        return fetchPlanDelegate.fetch(planDayArguments);
    }


    public Single<PlanMealItem> addToPlan(MealItem mealItem, String userId, LocalDate day) {
        String calendarUri = addToCalendar(mealItem, day);
        PlanDayEntity entity = new PlanDayEntity();
        entity.active = true;
        entity.mealId = mealItem.getId();
        entity.userId = userId;
        entity.day = day;
        entity.calendarUri = calendarUri;
        PlanMealItem item = new PlanMealItem(calendarUri, userId, day, mealItem, true);
        return planDayDAO
                .insertAll(entity)
                .andThen(backupToRemote(userId, Collections.singletonList(item)))
                .andThen(Single.just(item))
                .subscribeOn(Schedulers.io());
    }

    public Single<PlanMealItem> removeFromPlan(PlanMealItem mealItem) {
        PlanDayEntity entity = new PlanDayEntity();
        entity.active = false;
        entity.mealId = mealItem.getMeal().getId();
        entity.userId = mealItem.getUserId();
        entity.day = mealItem.getDay();
        removeFromCalendar(mealItem);
        PlanMealItem item = mealItem.copy();
        item.setCalendarUri(null);
        item.setActive(false);
        return planDayDAO
                .updateAll(entity)
                .andThen(backupToRemote(mealItem.getUserId(), Collections.singletonList(item)))
                .andThen(Single.just(item))
                .doAfterSuccess(itemResult -> {

                })
                .subscribeOn(Schedulers.io());
    }

    private String addToCalendar(MealItem mealItem, LocalDate day) {
        if (mealCalenderHelper != null) {
            return mealCalenderHelper.addMeal(day, mealItem);
        }
        return null;
    }

    private void removeFromCalendar(PlanMealItem mealItem) {
        if (mealCalenderHelper != null && mealItem.getCalendarUri() != null) {
            mealCalenderHelper.removeMeal(mealItem.getCalendarUri());
        }
    }

    private Completable backupToRemote(String userId, List<PlanMealItem> items) {
        Log.d(TAG, "backupToRemote: backing up");
        return planBackupService.insertForUser(userId, items).subscribeOn(Schedulers.io());
    }

    public Completable getBackupFromRemote(String userId) {
        Log.d(TAG, "getBackupFromRemote: backing up");
        return remoteBackupDelegate.fetch(userId).subscribeOn(Schedulers.io()).firstOrError().ignoreElement();
    }
}
