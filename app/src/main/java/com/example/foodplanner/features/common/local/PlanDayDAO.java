package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.Query;

import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.entities.PlanDayEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlanDayDAO {
    @Query("SELECT * FROM plandayentity JOIN mealitementity on plandayentity.mealId = mealitementity.id")
    @MapInfo(keyColumn = "day")
    Flowable<Map<LocalDate, List<MealItemEntity>>> getAll();

    @Insert
    Completable insertAll(PlanDayEntity... days);

    @Delete
    Completable delete(PlanDayEntity day);
}
