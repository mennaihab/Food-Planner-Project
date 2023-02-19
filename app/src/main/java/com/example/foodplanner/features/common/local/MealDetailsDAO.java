package com.example.foodplanner.features.common.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplanner.features.common.entities.MealEntity;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
@Dao
public interface MealDetailsDAO {

    @Query("SELECT * FROM MealEntity")
    Flowable<List<MealEntity>> getAll();

    @Query("SELECT * FROM MealEntity WHERE id == :id LIMIT 1")
    Flowable<Optional<MealEntity>> getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(MealEntity... meals);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<MealEntity> meals);

    @Delete
    Completable delete(MealEntity mealDetails);

}
