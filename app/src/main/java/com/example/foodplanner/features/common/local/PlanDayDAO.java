package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodplanner.features.common.entities.FavouriteMealEntity;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.entities.PlanDayEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlanDayDAO {
    @Query("SELECT * FROM plandayentity WHERE userId = :userId AND active = 1")
    @MapInfo(keyColumn = "day")
    Flowable<Map<LocalDate, List<MealItemEntity>>> getAllActive(String userId);

    @Query("SELECT * FROM plandayentity WHERE userId = :userId")
    Flowable<List<PlanDayEntity.Full>> getAll(String userId);

    @Query("SELECT * FROM plandayentity WHERE userId = :userId AND day >= :start AND day <= :end")
    Flowable<List<PlanDayEntity.Full>> getAll(String userId, LocalDate start, LocalDate end);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(PlanDayEntity... days);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<PlanDayEntity> days);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAll(PlanDayEntity... days);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateAll(List<PlanDayEntity> days);

    @Delete
    Completable delete(PlanDayEntity day);
}
